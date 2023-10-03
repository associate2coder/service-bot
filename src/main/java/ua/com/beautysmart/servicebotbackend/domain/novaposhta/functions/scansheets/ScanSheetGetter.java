package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.scansheets;


import ua.com.beautysmart.servicebotbackend.domain.entities.Sender;

import java.util.List;

public interface ScanSheetGetter {

    List<ScanSheet> getScanSheets(Sender sender);

    List<ScanSheet> getScanSheets(Sender sender, int numberOfRecentDays);


}
