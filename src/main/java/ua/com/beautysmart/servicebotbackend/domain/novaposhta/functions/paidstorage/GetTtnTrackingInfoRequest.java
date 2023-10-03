package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;


import ua.com.beautysmart.servicebotbackend.domain.novaposhta.common.NovaPoshtaBaseRequest;

import java.util.List;

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

