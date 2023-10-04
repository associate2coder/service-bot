package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScanSheetServiceImpl implements ScanSheetService {

    private final NovaPoshtaRequestSender requestSender;


    @Override
    public List<ScanSheet> getScanSheets(Sender sender) {
        return Arrays.stream(requestSender.send(new GetScanSheetRequest(sender.getApiKey())).getData())
                .toList();
    }

    // 0 - today, 2 -> today and two preceding days
    @Override
    public List<ScanSheet> getScanSheets(Sender sender, int numberOfRecentDays) {
        LocalDate startDate = LocalDate.now().minusDays(numberOfRecentDays);
        return Arrays.stream(requestSender.send(new GetScanSheetRequest(sender.getApiKey())).getData())
                .filter(r -> isToday(r.getDateTime()) ||
                        parseDate(r.getDateTime()).isEqual(startDate) ||
                        parseDate(r.getDateTime()).isAfter(startDate))
                .toList();
    }

    // date comes from Nova Poshta in format "yyyy-MM-dd HH:mm:ss"
    private LocalDate parseDate(String date) {
        return LocalDate.parse(date.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private boolean isToday(String date) {
        return LocalDate.now().equals(parseDate(date));
    }


}
