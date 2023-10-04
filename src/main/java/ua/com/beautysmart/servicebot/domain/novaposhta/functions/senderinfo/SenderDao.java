package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import lombok.Data;

@Data
public class SenderDao {

    private String Description; // to be used as full name
    private String Ref; // to be used as Ref
    private String City;
    private String Counterparty;
    private String FirstName;
    private String LastName;
    private String CounterpartyFullName;
    private String OwnershipFormRef;
    private String OwnershipFormDescription;
    private String EDRPOU;
    private String CounterpartyType;
    private String TrustedCounterpartyType;
    private String CityDescription;
}
