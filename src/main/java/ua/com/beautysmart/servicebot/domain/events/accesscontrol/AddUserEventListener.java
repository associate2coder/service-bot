package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.AccessRequest;
import ua.com.beautysmart.servicebot.domain.services.AccessRequestService;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AddUserEventListener {

    private final AccessValidationService accessValidationService;
    private final MenuContextHolder contextHolder;
    private final UserService userService;
    private final AccessRequestService requestService;
    private final MessageUtil messageUtil;

    @EventListener
    public void handleAddUserConfirmedEvent(AddUserConfirmedEvent event) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        Context context = contextHolder.getContext(chatId);
        long accessRequestId = Long.parseLong(context.getValue());
        AccessRequest request = requestService.getById(accessRequestId);
        userService.addUser(request.getUserId(), request.getUsername(), "user");
        requestService.deleteRequest(request.getId());
    }

    @EventListener
    public void handleAddUserEvent(AddUserEvent event) {

        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        accessValidationService.validateAdminAccess(update);

        Context context = contextHolder.getContext(chatId);

        switch (context.getMenuLevel()) {
            case 2: listAllRequests(update, context);
            case 3: {
                if ("DeleteAll".equals(context.getValue())) {
                    deleteAllRequests(update);
                } else {
                    confirmAndAdd(update, context);
                }
            }
        }
    }

    private void listAllRequests(Update update, Context context) {
        List<AccessRequest> requests = requestService.getAll();
        InlineKeyboardMarkup replyMarkup = createKeyboardForPendingRequests(requests);
        String text = "Поточні запити на надання доступу: ";
        if (context.isBack()) {
            long chatId = TgUtils.getChatIdFromUpdate(update);
            messageUtil.createAndSendNewMessage(chatId, text, replyMarkup);
        } else {
            Message message = TgUtils.getMessageFromUpdate(update);
            messageUtil.createAndSendEditMessageText(message, text, replyMarkup);
        }
    }

    private InlineKeyboardMarkup createKeyboardForPendingRequests(List<AccessRequest> requests) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (AccessRequest request: requests) {
            String text = String.format("%s %s %s",
                    request.getPhoneNumber(),
                    request.getName(),
                    request.getUsername());
            keyboard.add(KeyboardUtils.createInlineButtonRow(
                    KeyboardUtils.createInlineButton(text, "Type-->AddUser///Level-->3///Value-->" + request.getId())
            ));
        }
        keyboard.add(KeyboardUtils.createInlineButtonRow(
                KeyboardUtils.createInlineButton("Видалити всі запити", "Type-->AddUser///Level-->3///Value-->DeleteAll")
        ));
        keyboard.add(KeyboardUtils.createBackToMainInlineButtonRow());
        return KeyboardUtils.createInlineReplyMarkup(keyboard);
    }

    private void confirmAndAdd(Update update, Context context) {
        long chatId = TgUtils.getChatIdFromUpdate(update);

        long accessRequestId = Long.parseLong(context.getValue());
        AccessRequest request = requestService.getById(accessRequestId);
        String text = String.format(
                "Підтвердіть додавання в якості користувача бота:%n" +
                        "%s%n" +
                        "%s%n" +
                        "%s%n",
                request.getPhoneNumber(),
                request.getName(),
                request.getUsername()
        );

        InlineKeyboardMarkup replyMarkup = createConfirmationKeyboard(request);
        Message message = TgUtils.getMessageFromUpdate(update);
        messageUtil.createAndSendEditMessageText(message, text, replyMarkup);
    }

    private InlineKeyboardMarkup createConfirmationKeyboard(AccessRequest request) {

        return KeyboardUtils.createInlineReplyMarkup(
                KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("ТАК, додати!", "Type-->AddUserConfirmed///Level-->3///Value-->" + request.getId())
                ),
                KeyboardUtils.createBackToMainInlineButtonRow()
        );
    }

    public void deleteAllRequests(Update update) {

        requestService.deleteAll();

        String text = "Усі запити на отримання доступу видалені.";
        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(KeyboardUtils.createBackToMainInlineButtonRow());
        Message message = TgUtils.getMessageFromUpdate(update);

        messageUtil.createAndSendEditMessageText(message, text, replyKeyboard);
    }

}
