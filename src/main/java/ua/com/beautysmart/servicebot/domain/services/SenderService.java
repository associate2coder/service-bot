package ua.com.beautysmart.servicebot.domain.services;

import ua.com.beautysmart.servicebot.domain.entities.Sender;

import java.util.List;

public interface SenderService {

    Sender addSender(String phone, String alias, String apiKey);
    Sender updatePhone(String phone, String newPhone);
    Sender updateApiKey(String phone, String newApiKey);
    Sender getSender(String phone);
    List<Sender> getAllSenders();
    void removeSender(long id);


}
