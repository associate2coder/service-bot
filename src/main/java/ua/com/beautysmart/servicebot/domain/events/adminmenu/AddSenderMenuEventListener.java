package ua.com.beautysmart.servicebot.domain.events.adminmenu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaRequestSender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.GetSenderInfoRequest;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderDao;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderRequest;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AddSenderMenuEventListener {

    private final AccessValidationService accessValidationService;
    private final SenderService senderService;
    private final MenuContextHolder contextHolder;
    private final NovaPoshtaRequestSender requestSender;
    private final MessageUtil messageUtil;

    // TODO logic in this class should be simplified. Too complicated
    @EventListener
    public void handleAddSenderMenuEvent(AddSenderMenuEvent event) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);
        Context context =  contextHolder.getContext(chatId);

        // if user is not authorized, exception is thrown
        accessValidationService.validateAdminAccess(update);

        if (update.hasCallbackQuery()) {

            // if callBackData indicates that all necessary info is provided and confirmed, Sender is added to DB
            int menuLevel = context.getMenuLevel();
            String value = context.getValue();
            if ("AddSenderConfirmed".equals(value) && menuLevel == 3) {
                addSenderRequestConfirmed(update);
            }

            // "Adding Sender" logic starts when update has callback query from the button
            else if ("AddSenderIntro".equals(value) && menuLevel == 2) {
                addSenderIntro(update);
            }
        }

        // Since data is entered by admin in messages, there should be no callback query.
        else if (update.hasMessage()) {

            // *** 1 *** // TODO add comments with numbers as this code is hard
            // retrieve pending sender create request data
            SenderRequest newSender = context.getAddedSender();

            if (newSender.getPhone() == null && newSender.getAlias() == null && newSender.getApiKey() == null) {
                // if name is entered wrong (and nothing has been entered yet), loop will continue until entered correctly or process is cancelled
                checkPhoneAndProceed(update);
            } else if (newSender.getAlias() == null && newSender.getApiKey() == null) {
                // once phone number is added, proceed with entering apiKey
                checkApiKeyAndProceed(update);
            } else if (newSender.getAlias() == null) {
                // once apiKey is added, proceed with entering alias for the Sender
                checkAliasAndProceed(update);
            }
        }
    }

    // --> 1 <--
    // sends intro message and asks to enter phone number
    private void addSenderIntro(Update update) {

        long chatId = TgUtils.getChatIdFromUpdate(update);

        // init new SenderRequest instance in the context to be filled in
        contextHolder.getContext(chatId).setAddedSender(new SenderRequest());


        String introText = """
                Ключ ФОП необхідно додати у такій послідовності:
                - номер телефону
                - ключ API з кабінету нової пошти
                - alias - коротке ім'я для зручності (буде на кнопках)""";

        messageUtil.createAndSendNewMessage(chatId, introText);

        String enterPhoneText = "Введіть номер телефону у форматі 380ХХХХХХХХХ:";
        messageUtil.createAndSendNewMessage(chatId, enterPhoneText);
    }

    private void checkPhoneAndProceed(Update update) {
        String phoneNumber = update.getMessage().getText();

        Pattern phonePattern = Pattern.compile("380\\d{9}");
        Matcher matcher = phonePattern.matcher(phoneNumber);

        if (matcher.matches()) {
            numberAdded(update);
        } else {
            mistakeInPhone(update);
        }
    }

    private void mistakeInPhone(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        String phone = update.getMessage().getText();
        String text = String.format("""
                Помилка у форматі номеру телефону %s.

                Введіть номер телефону у форматі 380ХХХХХХХХХ:""", phone);
        messageUtil.createAndSendNewMessage(chatId, text);
    }

    private void numberAdded(Update update) {

        // if number is correct, it is added to context
        long chatId = TgUtils.getChatIdFromUpdate(update);
        SenderRequest newSender = contextHolder.getContext(chatId).getAddedSender();
        newSender.setPhone(update.getMessage().getText());

        // proceed with "adding" process for apiKey
        String text = "ключ API з кабінету нової пошти:";

        messageUtil.createAndSendNewMessage(chatId, text);
    }

    private void checkApiKeyAndProceed(Update update) {
        String apiKey = update.getMessage().getText();

        if (checkIfApiKeyValid(apiKey)) {
            apiKeyAdded(update);
        } else {
            mistakeInApiKey(update);
        }
    }

    private boolean checkIfApiKeyValid(String apiKey) {
        try {
            SenderDao senderDao = requestSender.send(new GetSenderInfoRequest(apiKey)).getData()[0];
            // if senderDao returns with information, apiKey is valid
            log.debug("Api key search returned the following information: " + senderDao);
            return !senderDao.getRef().isEmpty();
        } catch (Exception e) {
            // if exception is thrown, apiKey is wrong (or server is down which makes it useless to add a sender anyway)
            return false;
        }
    }

    private void mistakeInApiKey(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        String text = """
                Помилка у ключі API Нової пошти.

                ключ API з кабінету нової пошти:""";
        messageUtil.createAndSendNewMessage(chatId, text);
    }

    private void apiKeyAdded(Update update) {

        // if number is correct, it is added to context
        long chatId = TgUtils.getChatIdFromUpdate(update);
        SenderRequest newSender = contextHolder.getContext(chatId).getAddedSender();
        newSender.setApiKey(update.getMessage().getText());

        String text = "Введіть alias (коротке ім'я для зручності, яке буде на кнопках):";
        messageUtil.createAndSendNewMessage(chatId, text);
    }

    private void checkAliasAndProceed(Update update) {
        String alias = update.getMessage().getText();

        boolean isCommand = alias.startsWith("/");

        if (isCommand) {
            mistakeInAlias(update);
        } else {
            aliasAdded(update);
        }
    }

    private void mistakeInAlias(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        String text = """
                Символ '/' не може бути в назві.
                
                Введіть коротку назву для відповідного ФОПа:""";
        messageUtil.createAndSendNewMessage(chatId, text);
    }


    private void aliasAdded(Update update) {
        // if number is correct, it is added to context
        long chatId = TgUtils.getChatIdFromUpdate(update);
        SenderRequest newSender = contextHolder.getContext(chatId).getAddedSender();
        newSender.setAlias(update.getMessage().getText());

        // "OK, proceed" / "Try again" keyboard
        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(
                KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("OK", "Type-->AddSender///Level-->3///Value-->AddSenderConfirmed"),
                        KeyboardUtils.createInlineButton("Почати заново", "Type-->AddSender///Level-->2///Value-->AddSenderIntro")
                )
        );

        String text = String.format("""
               Введена наступна інформація:
               
               - номер телефону: %s
               - коротка назва: %s
               - ключ api доданий: %b
               
               Підтвердьте, чи все вірно?""",
                newSender.getPhone(),
                newSender.getAlias(),
                !newSender.getApiKey().isBlank());

        messageUtil.createAndSendNewMessage(chatId, text, replyKeyboard);
    }

    private void addSenderRequestConfirmed(Update update) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        SenderRequest newSender = contextHolder.getContext(chatId).getAddedSender();

        Sender sender = senderService.addSender(newSender.getPhone(), newSender.getAlias(), newSender.getApiKey());
        String text = String.format("Ключ аpi для ФОП %s додано.", sender.getAlias());

        contextHolder.getContext(chatId).clearAddedSender();

        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(
                KeyboardUtils.createBackToMainInlineButtonRow());

        messageUtil.createAndSendNewMessage(chatId, text, replyKeyboard);
    }

    private InlineKeyboardMarkup getBackButtons() {
        // creating keyboard for "To main / back to Admin"
        return KeyboardUtils.createInlineReplyMarkup(
                // to main / back to previous
                KeyboardUtils.createBackToMainInlineButtonRow()
        );
    }
}
