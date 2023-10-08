package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainMenuEventListener {


    private final TelegramLongPollingBot bot;
    private final AccessValidationService accessValidationService;
    private final MenuContextHolder contextHolder;

    @EventListener
    public void handleMainMenuEvent(MainMenuEvent event) {

        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        //adding user context to the context holder
        Context context = new Context(chatId);
        context.setMenuType("MainMenu");
        contextHolder.addContext(context);


        // creating keyboard for the Main Menu
        InlineKeyboardMarkup replyKeyboard = InlineKeyboardUtils.createReplyMarkup(
                // first buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Реєстри сьогодні", "Type-->ScanSheetToday///Level-->1"),
                        InlineKeyboardUtils.createInlineButton("Реєстри за 3 дні", "Type-->ScanSheet3Days///Level-->1")
                ),
                // second buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Пл.зберігання завтра", "Type-->PaidStorage///Level-->1"),
                        InlineKeyboardUtils.createInlineButton("Пл.зберігання післязавтра", "Type-->PaidStorage///Level-->1")
                ),
                // if user is admin -> also adding admin buttons row
                (accessValidationService.isAdmin(chatId) ? InlineKeyboardUtils.createAdminButtonRow() : null)
        );

        // sending Main Menu message to the user
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text("Головне меню:")
                            .replyMarkup(replyKeyboard)
                            .build()
            );
            log.debug("Request to send Main Menu message has been sent to Telegram.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
