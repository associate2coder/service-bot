package ua.com.beautysmart.servicebotbackend.domain.novaposhta.common;

import java.net.http.HttpRequest;

public interface HttpRequestFactory {

    <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> HttpRequest createPostRequest(T request);
}
