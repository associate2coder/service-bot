package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;


import ua.com.beautysmart.servicebot.domain.entities.Sender;

import java.util.List;

/**
 * Author: associate2coder
 */

public interface ScanSheetGetter {

    List<ScanSheet> getScanSheets(Sender sender);

    List<ScanSheet> getScanSheets(Sender sender, int numberOfRecentDays);


}
