package ua.com.beautysmart.servicebotbackend.domain.novaposhta.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class NovaPoshtaRequestSenderImpl implements NovaPoshtaRequestSender{

    private final HttpClient httpClient;
    private final HttpRequestFactory httpRequestFactory;

    @Override
    public <T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> R send(T t) {

        HttpRequest request = httpRequestFactory.createPostRequest(t);

        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        if (response == null) {
            log.error("response is null");
        }

        ObjectMapper mapper = new ObjectMapper();

        R result;

        if (t.getCustomDeserializer() != null) {
            SimpleModule module = new SimpleModule();
            try {
                module.addDeserializer(t.getResponseObjectClass(), t.getCustomDeserializer().getConstructor().newInstance());
                mapper.registerModule(module);
                result = mapper.readValue(response.body(), t.getResponseClass());
                return result;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException | JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                result = mapper.readValue(response.body(), t.getResponseClass());
                return result;
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
