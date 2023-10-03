package ua.com.beautysmart.servicebotbackend.domain.novaposhta.common;

public interface NovaPoshtaRequestSender {

    <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> R send(T t);

}
