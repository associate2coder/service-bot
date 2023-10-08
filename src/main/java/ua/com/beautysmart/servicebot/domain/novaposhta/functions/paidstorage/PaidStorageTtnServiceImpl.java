package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaidStorageTtnServiceImpl implements PaidStorageTtnService {

    private final NovaPoshtaRequestSender requestSender;

    @Override
    public List<TTN> getTtnForPaidStorage(Sender sender, int days) {

        // TODO fix NPE here

        LocalDate now = LocalDate.now();
        LocalDate relevantDate = now.plusDays(days);
        LocalDate startDate = now.minusDays(4);
        List<TTN> resultList = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            LocalDate date = startDate.plusDays(i);
            List<TTN> filteredList = getAllTtns(sender, date)
                    .stream()
                    .filter(ttn -> ttn.getFirstDayPaidKeeping().toLocalDate().equals(relevantDate)
                            || ttn.getFirstDayPaidKeeping().toLocalDate().isAfter(LocalDate.now()))
                    .toList();
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

        List<TTN> ttnList = Arrays.stream(ttnArray)
                .filter(ttn -> ttn.getStatus().equals("7"))
                .toList();

        for (TTN ttn: ttnList) {
            LocalDateTime paidStorageDate = requestSender.send(
                    new GetTtnTrackingInfoRequest()
                            .setDocument(ttn.getNumber(), sender.getPhone())
            ).getData().getFirstDayPaidKeeping();
            ttn.setFirstDayPaidKeeping(paidStorageDate);
        }
        return ttnList;
    }
}
