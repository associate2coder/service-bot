package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.CustomRuntimeException;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetUtils {

    private final SenderService senderService;
    private final ScanSheetService scanSheetService;

    public Map<Sender, List<ScanSheet>> retrieveScanSheets(boolean today) {
        List<Sender> senders = senderService.getAllSenders();
        Map<Sender, List<ScanSheet>> scanSheets = new HashMap<>();

        int days = today ? 0 : 2;
        log.debug("ScanSheets will be retrieved for " + (today ? "today only." : "three recent days including today"));

        for (Sender sender: senders) {
            List<ScanSheet> senderScanSheets = scanSheetService.getScanSheets(sender, days);
            scanSheets.put(sender, senderScanSheets);
        }
        return scanSheets;
    }

    public Pair<Sender, ScanSheet> getSenderAndScanSheetByNumber(boolean today, String number) {
        Map<Sender, List<ScanSheet>> all = retrieveScanSheets(today);
        for (Map.Entry<Sender, List<ScanSheet>> entry: all.entrySet()) {
            for (ScanSheet scanSheet: entry.getValue()) {
                if (number.equals(scanSheet.getNumber())) {
                    return Pair.of(entry.getKey(), scanSheet);
                }
            }
        }
        throw new CustomRuntimeException("Unexpected exception: scanSheet not found with number " + number);
    }

    public List<ScanSheet> getScanSheetsFromMap(Map<Sender, List<ScanSheet>> map) {
        List<ScanSheet> list = new ArrayList<>();
        for (List<ScanSheet> item: map.values()) {
            list.addAll(item);
        }
        list.sort(Comparator.reverseOrder());
        return list;
    }
}
