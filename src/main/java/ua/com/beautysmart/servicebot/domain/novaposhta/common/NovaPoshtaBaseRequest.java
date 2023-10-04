package ua.com.beautysmart.servicebot.domain.novaposhta.common;

import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class NovaPoshtaBaseRequest<T extends NovaPoshtaBaseRequest<T, R, T1>, R extends NovaPoshtaBaseResponse, T1> {

    private final String baseUrl = "https://api.novaposhta.ua/v2.0/json/";
    private final Map<String, Object> methodProperties;
    private final Map<String, Object> bodyParams;
    private final Class<? extends R> responseClass;
    private final Class<? extends JsonDeserializer<T1>> customDeserializer;
    private final Class<T1> responseObjectClass;

    public NovaPoshtaBaseRequest(Class<? extends R> responseClass, Class<T1> responseObjectClass) {
        this(responseClass, responseObjectClass, null);
    }

    public NovaPoshtaBaseRequest(Class<? extends R> responseClass, Class<T1> responseObjectClass, Class<? extends JsonDeserializer<T1>> customDeserializer) {
        this.methodProperties = new HashMap<>();
        this.bodyParams = new HashMap<>();
        this.responseClass = responseClass;
        this.customDeserializer = customDeserializer;
        this.responseObjectClass = responseObjectClass;
        bodyParams.put("methodProperties", this.methodProperties);
    }

    public NovaPoshtaBaseRequest<T, R, T1> setApiKey(String apiKey) {
        this.bodyParams.put("apiKey", apiKey);
        return this;
    }

    public NovaPoshtaBaseRequest<T, R, T1> addMethodProperty(String key, Object value) {
        this.methodProperties.put(key, value);
        return this;
    }

    protected NovaPoshtaBaseRequest<T, R, T1> setModelName(String modelName) {
        getBodyParams().put("modelName", modelName);
        return this;
    }

    protected NovaPoshtaBaseRequest<T, R, T1> setCalledMethod(String calledMethod) {
        getBodyParams().put("calledMethod", calledMethod);
        return this;
    }
}
