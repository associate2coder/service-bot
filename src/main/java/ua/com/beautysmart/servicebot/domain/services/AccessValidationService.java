package ua.com.beautysmart.servicebot.domain.services;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public interface AccessValidationService {
    boolean isUser(long chatId);
    void validateUserAccess(Update update);
    boolean isAdmin(long chatId);
    void validateAdminAccess(Update update);

}
