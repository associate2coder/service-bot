package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * Author: associate2coder
 */

@Data
public class SenderDao {

    @JsonAlias(value = "Description")
    private String description; // to be used as full name

    @JsonAlias(value = "Ref")
    private String ref; // to be used as Ref

    @JsonAlias(value = "City")
    private String city;

    @JsonAlias(value = "Counterparty")
    private String counterparty;

    @JsonAlias(value = "FirstName")
    private String firstName;

    @JsonAlias(value = "LastName")
    private String lastName;

    @JsonAlias(value = "MiddleName")
    private String middleName;

    @JsonAlias(value = "CounterpartyFullName")
    private String counterpartyFullName;

    @JsonAlias(value = "OwnershipFormRef")
    private String ownershipFormRef;

    @JsonAlias(value = "OwnershipFormDescription")
    private String ownershipFormDescription;

    @JsonAlias(value = "EDRPOU")
    private String edrpou;

    @JsonAlias(value = "CounterpartyType")
    private String counterpartyType;

    @JsonAlias(value = "TrustedCounterpartyType")
    private String trustedCounterpartyType;

    @JsonAlias(value = "CityDescription")
    private String cityDescription;
}
