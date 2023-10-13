package ua.com.beautysmart.servicebot.domain.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.com.beautysmart.servicebot.domain.entities.AccessRequest;

import java.util.List;

/**
 * Author: associate2coder
 */

public interface AccessRequestService {

    AccessRequest addRequest(Message message);

    AccessRequest getById(long id);

    AccessRequest getByUserId(long userId);

    AccessRequest getByPhoneNumber(String phoneNumber);

    List<AccessRequest> getAll();

    void deleteRequest(long id);

    void deleteAll();

    boolean exists(String phoneNumber);

    boolean exists(long userId);
}
