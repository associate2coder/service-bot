package ua.com.beautysmart.servicebotbackend.domain.services;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AccessValidationService {
    boolean isUser(long chatId);
    void validateUserAccess(Update update);
    boolean isAdmin(long chatId);
    void validateAdminAccess(Update update);

}
