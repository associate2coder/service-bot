package ua.com.beautysmart.servicebotbackend.domain.novaposhta.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;

@Component
@Slf4j
public class NovaPoshtaHttpRequestFactoryImpl implements HttpRequestFactory {

    private final ObjectMapper mapper;

    public NovaPoshtaHttpRequestFactoryImpl() {
        this.mapper = new ObjectMapper();
    }


    @Override
    public <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> HttpRequest createPostRequest(T request) {
        try {

            return HttpRequest.newBuilder()
                    .uri(new URI(request.getBaseUrl()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request.getBodyParams())))
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
