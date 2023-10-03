package ua.com.beautysmart.servicebotbackend.domain.services;

import ua.com.beautysmart.servicebotbackend.domain.entities.Sender;
import ua.com.beautysmart.servicebotbackend.domain.entities.User;

import java.util.List;

public interface SenderService {

    Sender addSender(String phone, String name, String apiKey);
    Sender updatePhone(String phone, String newPhone);
    Sender updateApiKey(String phone, String newApiKey);
    Sender getSender(String phone);
    List<Sender> getAllSenders();
    void removeSender(long id);


}
