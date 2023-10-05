package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderRequest;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddSenderMenuEventListener {

    private final TelegramLongPollingBot bot;
    private final AccessValidationService accessValidationService;
    private final SenderService senderService;
    private final MenuContextHolder contextHolder;

    @EventListener
    public void handleAddSenderMenuEvent(AddSenderMenuEvent event) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);
        Context context =  contextHolder.getContext(chatId);

        // if user is not authorized, exception is thrown
        accessValidationService.validateAdminAccess(update);

        if (update.hasCallbackQuery()) {
            addSenderIntro(update);
        }
        if (update.hasMessage()) {
            SenderRequest newSender = context.getAddedSender();
            if (newSender.getPhone() == null && newSender.getName() == null && newSender.getApiKey() == null) {
                mistakeInName(update); //TODO
            } else if (newSender.getName() == null && newSender.getApiKey() == null) {
                numberAdded(update);
            } else if (newSender.getName() == null) {
                apiKeyAdded(update);
            } else {
                nameAdded(update);
            }
        }
        //TODO Add validations to phone / apiKey
    }

    private void addSenderIntro(Update update) {

        long chatId = TgUtils.getChatIdFromUpdate(update);

        String text1 = """
                Ключ ФОП необхідно додати у такій послідовності:
                - номер телефону
                - ключ API з кабінету нової пошти
                - коротке ім'я для зручності (буде на кнопках)""";

        String text2 = "Введіть номер телефону у форматі 380ХХХХХХХХХ:";



        // Sending edited Admin Menu message to the user
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text1)
                            .build()
            );

            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text2)
                            .replyMarkup(getBackButtons())
                            .build()
            );

            addSenderSendMessageLog();
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }


    private void numberAdded(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        String text = "ключ API з кабінету нової пошти:";

        try {

            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .replyMarkup(getBackButtons())
                            .build()
            );

            addSenderSendMessageLog();
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }

    private void apiKeyAdded(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        String text = "коротке ім'я для зручності (буде на кнопках):";

        try {

            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .replyMarkup(getBackButtons())
                            .build()
            );

            addSenderSendMessageLog();
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }

    private void nameAdded(Update update) {
        //TODO add confirmation with callBackData
    }

    private void senderAdded(Update update) {
        //TODO send confirmation that sender is added and clear context
    }



    private void addSenderSendMessageLog() {
        log.debug("Request to send message text to AddSender Menu has been sent to Telegram.");
    }

    private InlineKeyboardMarkup getBackButtons() {
        // creating keyboard for "To main / back to Admin"
        return InlineKeyboardUtils.createReplyMarkup(
                // to main / back to previous
                InlineKeyboardUtils.createBackToMainButtonRow()
        );
    }



}
