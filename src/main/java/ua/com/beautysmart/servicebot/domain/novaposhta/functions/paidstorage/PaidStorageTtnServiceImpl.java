package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Author: associate2coder
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class PaidStorageTtnServiceImpl implements PaidStorageTtnService {

    private final NovaPoshtaRequestSender requestSender;
    // number of days after NOW after which paid storage will commence (1 day = paid storage is tomorrow).
    private final static int DAYS_UNTIL_PAID_STORAGE = 1;
    // number of days when search for TTN should be started. TTN during these days should be bulletproof in terms of paid storage (to be checked with Nova Poshta)
    private final static int NUMBER_OF_BULLETPROOF_DAYS = 4;

    @Override
    public List<TTN> getTtnForPaidStorage(Sender sender) {

        LocalDate now = LocalDate.now();
        LocalDate relevantDate = now.plusDays(DAYS_UNTIL_PAID_STORAGE);
        LocalDate startDate = now.minusDays(NUMBER_OF_BULLETPROOF_DAYS);
        List<TTN> resultList = new ArrayList<>();

        // iterates 9 days starting from the end of the bulletproof days.
        // 9 is magic number. It is assumed that previously created TTN with paid storage have been identified and dealt with
        for (int i = 0; i < 9; i++) {
            LocalDate date = startDate.minusDays(i);
            List<TTN> filteredList = getAllTtns(sender, date)
                    .stream()
                    .peek(ttn -> log.debug("filtering TTN: " + ttn.toString()))
                    .filter(ttn -> (
                            // paid storate starts tomorrow
                            ttn.getFirstDayPaidKeeping().toLocalDate().equals(relevantDate)
                                    // or paid storage has already begun
                                    || ttn.getFirstDayPaidKeeping().toLocalDate().isBefore(relevantDate))
                            //&& ttn.getFirstDayPaidKeeping().toLocalDate().isAfter(LocalDate.now())) // TODO delete after checks
                    )
                    .toList();
            if (filteredList.isEmpty()) {
                continue;
            }
            resultList.addAll(filteredList);
        }
        return resultList;
    }

    // function to get all TTNs created by the relevant sender on a relevant date
    private List<TTN> getAllTtns(Sender sender, LocalDate date) {

        GetPendingTtnRequest ttnRequest = new GetPendingTtnRequest(sender.getApiKey())
                .setDateFrom(date)
                .setDateTo(date)
                .setGetFullList(true);

        TTN[] ttnArray = requestSender.send(ttnRequest).getData();

        // filter to only those TTN which are arrived and waiting to be picked up
        List<TTN> ttnList = new ArrayList<>(Arrays.stream(ttnArray)
                .filter(ttn -> ttn.getStatus().equals("7")) // 7 = status code "Arrived to Nova Poshta post office"
                .toList()
        );

        // no need to continue any further actions if relevant list is empty.
        if (ttnList.isEmpty()) {
            return ttnList;
        }

        // if list is not empty, each TTN should be updated with paid storage information
        ListIterator<TTN> it = ttnList.listIterator();
        while (it.hasNext()) {

            TTN ttn = it.next();
            log.debug("TTN being checked is: " + ttn.getNumber());
            TtnTracking info = requestSender.send(new GetTtnTrackingInfoRequest()
                    .setDocument(ttn.getNumber(), sender.getPhone()))
            .getData();

            // if information from Nova Poshta has automatic return, it will not have paid storage info. TTN should be skipped
            if (info.getFirstDayPaidKeeping() == null && info.getDateReturnCargo() != null) {
                it.remove();
                continue;
            }
            // if TTN has paid storage info, it is added to TTN object
            ttn.setFirstDayPaidKeeping(info.getFirstDayPaidKeeping());
        }
        // All TTNs are returned for further filtering
        return ttnList;
    }
}
