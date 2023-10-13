package ua.com.beautysmart.servicebot.domain.novaposhta.common;

/**
 * Author: associate2coder
 */

public interface NovaPoshtaRequestSender {

    <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> R send(T t);

}
