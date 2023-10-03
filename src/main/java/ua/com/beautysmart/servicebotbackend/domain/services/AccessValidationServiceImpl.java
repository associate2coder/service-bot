package ua.com.beautysmart.servicebotbackend.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebotbackend.domain.bot.common.Role;
import ua.com.beautysmart.servicebotbackend.domain.bot.exceptions.AccessRestrictedException;
import ua.com.beautysmart.servicebotbackend.domain.entities.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessValidationServiceImpl implements AccessValidationService {

    private final UserService userService;

    @Override
    public boolean isUser(long chatId) {
        User user = userService.getUser(chatId);
        return user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.USER);
    }

    @Override
    public void validateUserAccess(Update update) {
        long chatId = update.getMessage().getChatId();
        if (!isUser(chatId)) {
            throw new AccessRestrictedException("Action is not permitted. Access denied", update);
        }
    }

    @Override
    public boolean isAdmin(long chatId) {
        User user = userService.getUser(chatId);
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public void validateAdminAccess(Update update) {
        long chatId = update.getMessage().getChatId();
        if (!isAdmin(chatId)) {
            throw new AccessRestrictedException("Action is not permitted. Access denied", update);
        }
    }
}
