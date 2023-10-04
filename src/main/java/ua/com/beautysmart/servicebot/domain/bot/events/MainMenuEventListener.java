package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainMenuEventListener {


    private final TelegramLongPollingBot bot;
    private final UserService userService;
    private final AccessValidationService accessValidationService;
    private final MenuContextHolder contextHolder;

    @EventListener
    public void handleMainMenuEvent(MainMenuEvent event) {
        Update update = (Update) event.getSource();
        long chatId = update.getMessage().getChatId();

        //adding user context to the context holder
        Context context = new Context(chatId);
        context.setMenuType("MainMenu");
        contextHolder.addContext(context);

        // if user is not authorized, exception is thrown
        accessValidationService.validateUserAccess(update);

        // creating keyboard for the Main Menu
        ReplyKeyboard replyKeyboard = InlineKeyboardUtils.createReplyMarkup(
                // first buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Реєстри сьогодні", "MenuType-->ScanSheetToday///MenuLevel-->1"),
                        InlineKeyboardUtils.createInlineButton("Реєстри за 3 дні", "MenuType-->ScanSheet3Days///MenuLevel-->1")
                ),
                // second buttons row
                InlineKeyboardUtils.createButtonRow(
                        InlineKeyboardUtils.createInlineButton("Пл.зберігання завтра", "MenuType-->PaidStorage///MenuLevel-->1"), //TODO
                        InlineKeyboardUtils.createInlineButton("Пл.зберігання післязавтра", "MenuType-->PaidStorage///MenuLevel-->1") //TODO
                ),
                // if user is admin -> also adding admin buttons row
                (accessValidationService.isAdmin(update.getMessage().getChatId()) ? InlineKeyboardUtils.createAdminButtonRow() : null)
        );

        // sending Main Menu message to the user
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .text("Головне меню:")
                            .replyMarkup(replyKeyboard)
                            .build()
            );
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
