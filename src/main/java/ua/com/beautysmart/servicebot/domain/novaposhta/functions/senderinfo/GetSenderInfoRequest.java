package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseRequest;

public class GetSenderInfoRequest extends NovaPoshtaBaseRequest<GetSenderInfoRequest, GetSenderInfoResponse, SenderDao[]> {

    public GetSenderInfoRequest(String apiKey) {
        super(GetSenderInfoResponse.class, SenderDao[].class);
        setModelName("Counterparty");
        setCalledMethod("getCounterparties");
        setApiKey(apiKey);
        addMethodProperty("CounterpartyProperty", "Sender");
    }
}
