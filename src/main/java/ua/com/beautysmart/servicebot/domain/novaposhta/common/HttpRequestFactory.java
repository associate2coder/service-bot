package ua.com.beautysmart.servicebot.domain.novaposhta.common;

import java.net.http.HttpRequest;

/**
 * Author: associate2coder
 */

public interface HttpRequestFactory {

    <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> HttpRequest createPostRequest(T request);
}
