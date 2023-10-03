package ua.com.beautysmart.servicebotbackend.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebotbackend.domain.entities.Sender;
import ua.com.beautysmart.servicebotbackend.domain.novaposhta.common.NovaPoshtaRequestSender;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService{

    private final NovaPoshtaRequestSender requestSender;

    @Override
    public Sender addSender(String phone, String name, String apiKey) {
        return null;
    }

    @Override
    public Sender updatePhone(String phone, String newPhone) {
        return null;
    }

    @Override
    public Sender updateApiKey(String phone, String newApiKey) {
        return null;
    }

    @Override
    public Sender getSender(String phone) {
        return null;
    }

    @Override
    public List<Sender> getAllSenders() {
        return null;
    }

    @Override
    public void removeSender(long id) {

    }
}
