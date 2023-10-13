package ua.com.beautysmart.servicebot.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.com.beautysmart.servicebot.domain.exceptions.ElementNotFoundException;
import ua.com.beautysmart.servicebot.domain.entities.AccessRequest;
import ua.com.beautysmart.servicebot.persistence.AccessRequestRepository;

import java.util.List;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessRequestServiceImpl implements AccessRequestService{

    private final AccessRequestRepository requestRepo;

    @Override
    public AccessRequest addRequest(Message message) {
        String username = message.getFrom().getUserName();
        Contact contact = message.getContact();
        return requestRepo.save(AccessRequest.builder()
                        .userId(contact.getUserId())
                        .phoneNumber(contact.getPhoneNumber())
                        .name(contact.getFirstName() + " " + contact.getLastName())
                        .username("@" + username)
                .build());
    }
    @Override
    public AccessRequest getById(long id) {
        return requestRepo.findById(id).orElseThrow(() -> new ElementNotFoundException("Candidate has not been found in the database"));
    }
    @Override
    public AccessRequest getByUserId(long userId) {
        return requestRepo.findByUserId(userId).orElseThrow(() -> new ElementNotFoundException("Candidate has not been found in the database"));
    }
    @Override
    public AccessRequest getByPhoneNumber(String phoneNumber) {
        return requestRepo.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ElementNotFoundException("Candidate has not been found in the database"));
    }

    @Override
    public List<AccessRequest> getAll() {
        return requestRepo.findAll();
    }

    @Override
    public void deleteRequest(long id) {
        requestRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        requestRepo.deleteAll();
    }


    @Override
    public boolean exists(String phoneNumber) {
        return requestRepo.existsByPhoneNumber(phoneNumber);
    }
    @Override
    public boolean exists(long userId) {
        return requestRepo.existsByUserId(userId);
    }
}
