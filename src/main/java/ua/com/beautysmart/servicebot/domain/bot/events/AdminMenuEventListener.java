package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMenuEventListener {


    private final TelegramLongPollingBot bot;
    private final AccessValidationService accessValidationService;

    @EventListener
    public void handleAdminMenuEvent(AdminMenuEvent event) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        // if user is not authorized, exception is thrown
        accessValidationService.validateAdminAccess(update);

        // creating keyboard for the Admin Menu
        InlineKeyboardMarkup replyKeyboard = InlineKeyboardUtils.createReplyMarkup(
                // first buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Додати користувача", "Type-->AddUser///MenuLevel-->2")
                ),
                // second buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Додати ключ ФОП", "Type-->AddSender///Level-->2///Value-->AddSenderIntro")
                ),
                // to main / back to previous
                InlineKeyboardUtils.createMainAsBackButtonRow()
        );

        // Sending edited Admin Menu message to the user
        try {
            bot.execute(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(TgUtils.getMessageFromUpdate(update).getMessageId())
                            .text("Панель адміністрування.\n\nВиберіть опцію:")
                            .replyMarkup(replyKeyboard)
                            .build()
            );
            log.debug("Request to edit message text to Admin Menu has been sent to Telegram.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
