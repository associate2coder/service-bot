package ua.com.beautysmart.servicebot.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebot.domain.bot.common.Role;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.AccessRestrictedException;
import ua.com.beautysmart.servicebot.domain.entities.User;

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
        long chatId = TgUtils.getChatIdFromUpdate(update);
        if (!isUser(chatId)) {
            throw new AccessRestrictedException("Action is not permitted. Access denied", update);
        }
        log.debug("Access granted to user with chat ID: " + chatId);
    }

    @Override
    public boolean isAdmin(long chatId) {
        return userService.getUser(chatId).getRole().equals(Role.ADMIN);
    }

    @Override
    public void validateAdminAccess(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        if (!isAdmin(chatId)) {
            throw new AccessRestrictedException("Action is not permitted. Access denied", update);
        }
        log.debug("Access granted to admin with chat ID: " + chatId);
    }
}
