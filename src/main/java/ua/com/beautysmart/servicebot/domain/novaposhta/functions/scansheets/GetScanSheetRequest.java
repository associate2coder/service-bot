package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;

import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseRequest;

public class GetScanSheetRequest extends NovaPoshtaBaseRequest<GetScanSheetRequest, GetScanSheetResponse, ScanSheet[]> {

    public GetScanSheetRequest(String apiKey) {
        super(GetScanSheetResponse.class, ScanSheet[].class);
        setModelName("ScanSheet");
        setCalledMethod("getScanSheetList");
        setApiKey(apiKey);
    }
}
