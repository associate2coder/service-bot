package ua.com.beautysmart.servicebotbackend.domain.novaposhta.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public abstract class NovaPoshtaBaseResponse {

    private boolean success;

    @JsonIgnore
    private String[] errors;
    @JsonIgnore
    private String[] warnings;
    @JsonIgnore
    private String[] info;
    @JsonIgnore
    private String[] messageCodes;
    @JsonIgnore
    private String[] errorCodes;
    @JsonIgnore
    private String[] warningCodes;
    @JsonIgnore
    private String[] infoCodes;
}
