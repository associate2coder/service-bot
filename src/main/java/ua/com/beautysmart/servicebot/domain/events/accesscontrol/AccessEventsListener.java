package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.services.AccessRequestService;

import java.util.List;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessEventsListener {

    private final MessageUtil messageUtil;
    private final AccessRequestService accessRequestService;

    @EventListener
    public void handleAccessRestrictedEvent(AccessRestrictedEvent event) {
        // retrieve update from Event source
        Update update = (Update) event.getSource();
        // get chatId from update
        long chatId = TgUtils.getChatIdFromUpdate(update);
        // prepare message text
        String text = "Доступ обмежено.";
        // message is sent
        messageUtil.createAndSendNewMessage(chatId, text);
    }

    @EventListener
    public void handleRequestAccessEvent(RequestAccessEvent event) {

        log.debug(event.toString() + ": commencement of handling of an Request Access Event");

        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        if (accessRequestService.exists(chatId)) {
            AccessRequestAlreadySent(update);
        } else {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasContact()) {
                    accessRequestService.addRequest(message);
                    messageUtil.createAndSendNewMessage(message.getChatId(), "Запит на отримання доступу був направлений");
                    return;
                }
            }

            ReplyKeyboardMarkup replyMarkup = ReplyKeyboardMarkup.builder()
                    .keyboard(
                            List.of(
                                    new KeyboardRow(
                                            List.of(
                                                    KeyboardButton.builder()
                                                            .text("Отримати доступ")
                                                            .requestContact(true)
                                                            .build()
                                            )
                                    )
                            )
                    ).build();
            replyMarkup.setResizeKeyboard(true);

            messageUtil.createAndSendNewMessage(chatId, "Закритий розділ. Доступ обмежено.", replyMarkup);
        }
        log.debug(event.toString() + ": Request Access Event has been handled");
    }

    private void AccessRequestAlreadySent(Update update) {
        String text = "Запит на отримання доступу вже направлений";
        long chatId = TgUtils.getChatIdFromUpdate(update);
        messageUtil.createAndSendNewMessage(chatId, text);
    }
}
