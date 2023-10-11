package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaidStorageTtnServiceImpl implements PaidStorageTtnService {

    private final NovaPoshtaRequestSender requestSender;

    @Override
    public List<TTN> getTtnForPaidStorage(Sender sender, int days) {
        
        LocalDate now = LocalDate.now();
        LocalDate relevantDate = now.plusDays(days);
        LocalDate startDate = now.minusDays(4);
        List<TTN> resultList = new ArrayList<>();


        for (int i = 0; i < 9; i++) {
            LocalDate date = startDate.minusDays(i);
            List<TTN> filteredList = getAllTtns(sender, date)
                    .stream()
                    .peek(ttn -> log.debug("filtering TTN: " + ttn.toString()))
                    .filter(ttn -> (
                            ttn.getFirstDayPaidKeeping().toLocalDate().equals(relevantDate)
                                    || ttn.getFirstDayPaidKeeping().toLocalDate().isBefore(relevantDate))
                            && ttn.getFirstDayPaidKeeping().toLocalDate().isAfter(LocalDate.now()))
                    .toList();
            if (filteredList.isEmpty()) {
                continue;
            }
            resultList.addAll(filteredList);
        }
        return resultList;
    }

    private List<TTN> getAllTtns(Sender sender, LocalDate date) {

        GetPendingTtnRequest ttnRequest = new GetPendingTtnRequest(sender.getApiKey())
                .setDateFrom(date)
                .setDateTo(date)
                .setGetFullList(true);

        TTN[] ttnArray = requestSender.send(ttnRequest).getData();

        List<TTN> ttnList = new ArrayList<>(Arrays.stream(ttnArray)
                .filter(ttn -> ttn.getStatus().equals("7"))
                .toList()
        );

        if (ttnList.isEmpty()) {
            return ttnList;
        }

        ListIterator<TTN> it = ttnList.listIterator();
        while (it.hasNext()) {
            int index = it.nextIndex();
            TTN ttn = it.next();


            log.debug("TTN being checked is: " + ttn.getNumber());

            var request = new GetTtnTrackingInfoRequest()
                    .setDocument(ttn.getNumber(), sender.getPhone());
            var response = requestSender.send(request);
            TtnTracking info = response.getData();
            if (info.getFirstDayPaidKeeping() == null && info.getDateReturnCargo() != null) {
                it.remove();
                continue;
            }
            LocalDateTime paidStorageDate = response.getData().getFirstDayPaidKeeping();
            ttn.setFirstDayPaidKeeping(paidStorageDate);
        }
        return ttnList;
    }
}
