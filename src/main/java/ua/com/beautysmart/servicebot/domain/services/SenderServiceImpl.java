package ua.com.beautysmart.servicebot.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.SenderNotFoundException;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.GetSenderInfoRequest;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderDao;
import ua.com.beautysmart.servicebot.persistence.SenderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService{

    private final NovaPoshtaRequestSender requestSender;
    private final SenderRepository senderRepo;

    @Override
    public Sender addSender(String phone, String name, String apiKey) {
        SenderDao senderDao = requestSender.send(new GetSenderInfoRequest(apiKey)).getData()[0];
        return senderRepo.save
                (Sender.builder()
                        .phone(phone)
                        .name(name)
                        .apiKey(apiKey)
                        .Ref(senderDao.getRef())
                        .fullName(senderDao.getDescription())
                        .build()
                );
    }

    @Override
    public Sender updatePhone(String phone, String newPhone) {
        Sender sender = getSender(phone);
        sender.setPhone(newPhone);
        return senderRepo.save(sender);
    }

    @Override
    public Sender updateApiKey(String phone, String newApiKey) {
        Sender sender = getSender(phone);
        sender.setApiKey(newApiKey);
        return senderRepo.save(sender);    }

    @Override
    public Sender getSender(String phone) {
        return senderRepo.findByPhone(phone)
                .orElseThrow(() -> new SenderNotFoundException("Sender not found."));
    }

    @Override
    public List<Sender> getAllSenders() {
        return senderRepo.findAll();
    }

    @Override
    public void removeSender(long id) {
        if (senderRepo.existsById(id)) {
            senderRepo.deleteById(id);
        }
    }
}
