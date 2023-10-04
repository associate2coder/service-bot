package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import lombok.Getter;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseResponse;

@Getter
public class GetSenderInfoResponse extends NovaPoshtaBaseResponse {

    private SenderDao[] data;

}
