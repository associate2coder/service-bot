package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import lombok.Getter;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseResponse;

/**
 * Author: associate2coder
 */

@Getter
public class GetPendingTtnResponse extends NovaPoshtaBaseResponse {

    private TTN[] data;
}
