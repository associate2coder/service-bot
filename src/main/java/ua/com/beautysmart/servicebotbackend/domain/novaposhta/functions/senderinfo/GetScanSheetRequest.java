package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.senderinfo;

import ua.com.beautysmart.servicebotbackend.domain.novaposhta.common.NovaPoshtaBaseRequest;

public class GetScanSheetRequest extends NovaPoshtaBaseRequest<GetScanSheetRequest, GetScanSheetResponse, ScanSheet[]> {

    public GetScanSheetRequest(String apiKey) {
        super(GetScanSheetResponse.class, ScanSheet[].class);
        setModelName("ScanSheet");
        setCalledMethod("getScanSheetList");
        setApiKey(apiKey);
    }
}
