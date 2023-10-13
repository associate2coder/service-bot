package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Author: associate2coder
 */

public class GetPendingTtnRequest extends NovaPoshtaBaseRequest<GetPendingTtnRequest, GetPendingTtnResponse, TTN[]> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public GetPendingTtnRequest(String apiKey) {
        super(GetPendingTtnResponse.class, TTN[].class, TtnCustomDeserializer.class);
        setModelName("InternetDocument");
        setCalledMethod("getDocumentList");
        setApiKey(apiKey);
    }

    public GetPendingTtnRequest setDateFrom(LocalDate date) {
        addMethodProperty("DateTimeFrom", date.format(DATE_FORMATTER));
        return this;
    }
    public GetPendingTtnRequest setDateTo(LocalDate date) {
        addMethodProperty("DateTimeTo", date.format(DATE_FORMATTER));
        return this;
    }

    public GetPendingTtnRequest setRedeliveryMoney(boolean yes) {
        String value = yes? "1" : "0";
        addMethodProperty("RedeliveryMoney", value);
        return this;
    }

    public GetPendingTtnRequest setGetFullList(boolean yes) {
        String value = yes? "1" : "0";
        addMethodProperty("GetFullList", value);
        return this;
    }

    public GetPendingTtnRequest setGetFullList(int page) {
        addMethodProperty("GetFullList", "" + page);
        return this;
    }



}
