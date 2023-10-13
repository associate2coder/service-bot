package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import lombok.Getter;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseResponse;

/**
 * Author: associate2coder
 */

@Getter
public class GetSenderInfoResponse extends NovaPoshtaBaseResponse {

    private SenderDao[] data;

}
