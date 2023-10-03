package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;

import lombok.Getter;
import ua.com.beautysmart.servicebotbackend.domain.novaposhta.common.NovaPoshtaBaseResponse;

@Getter
public class GetPendingTtnResponse extends NovaPoshtaBaseResponse {

    private TTN[] data;
}
