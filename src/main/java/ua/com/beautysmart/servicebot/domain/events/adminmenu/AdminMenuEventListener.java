package ua.com.beautysmart.servicebot.domain.events.adminmenu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMenuEventListener {

    private final AccessValidationService accessValidationService;
    private final MenuContextHolder contextHolder;
    private final MessageUtil messageUtil;

    @EventListener
    public void handleAdminMenuEvent(AdminMenuEvent event) {

        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        // if user is not authorized, exception is thrown
        accessValidationService.validateAdminAccess(update);

        Context context = contextHolder.getContext(chatId);
        if (context.isBack()) {
            context.clear();
        }

        // creating keyboard for the Admin Menu
        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(
                // new buttons row
                KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("Додати користувача", "Type-->AddUser///Level-->2")
                ),
                // new buttons row
                KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("Додати адміністратора", "Type-->AddAdmin///Level-->2")
                ),
                // new buttons row
                KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("Додати ключ ФОП", "Type-->AddSender///Level-->2///Value-->AddSenderIntro")
                ),
                // to main / back to previous
                KeyboardUtils.createMainAsBackInlineButtonRow()
        );

        // Sending edited Admin Menu message to the user
        Message message = TgUtils.getMessageFromUpdate(update);
        String text = "Панель адміністрування.\n\nВиберіть опцію:";
        messageUtil.createAndSendEditMessageText(message, text, replyKeyboard);
    }
}
