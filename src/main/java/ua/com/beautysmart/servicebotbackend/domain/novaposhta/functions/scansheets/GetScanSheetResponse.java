package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.scansheets;

import lombok.Getter;
import ua.com.beautysmart.servicebotbackend.domain.novaposhta.common.NovaPoshtaBaseResponse;

@Getter
public class GetScanSheetResponse extends NovaPoshtaBaseResponse {

    private ScanSheet[] data;

}