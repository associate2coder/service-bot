package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;


import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseRequest;

import java.util.List;

/**
 * Author: associate2coder
 */

public class GetTtnTrackingInfoRequest extends NovaPoshtaBaseRequest<GetTtnTrackingInfoRequest, GetTtnTrackingInfoResponse, TtnTracking> {


    public GetTtnTrackingInfoRequest() {
        super(GetTtnTrackingInfoResponse.class, TtnTracking.class, TtnTrackingCustomDeserializer.class);
        setModelName("TrackingDocument");
        setCalledMethod("getStatusDocuments");
    }

    public GetTtnTrackingInfoRequest setDocument(String documentNumber, String phone) {
        getMethodProperties().put("Documents", List.of(new Document(documentNumber, phone)));
        return this;
    }

    private record Document(String DocumentNumber, String Phone) {}


}

