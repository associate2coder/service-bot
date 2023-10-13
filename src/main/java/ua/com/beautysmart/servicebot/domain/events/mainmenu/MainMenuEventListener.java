package ua.com.beautysmart.servicebot.domain.events.mainmenu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MainMenuEventListener {


    private final AccessValidationService accessValidationService;
    private final MenuContextHolder contextHolder;
    private final MessageUtil messageUtil;

    @EventListener
    public void handleMainMenuEvent(MainMenuEvent event) {

        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        accessValidationService.validateUserAccess(update);

        //adding user context to the context holder
        Context context = new Context(chatId);
        context.setMenuType("MainMenu");
        contextHolder.addContext(context);

        // creating keyboard for the Main Menu
        InlineKeyboardMarkup replyKeyboard = createMainMenuKeyboard(chatId);

        // sending Main Menu message to the user
        messageUtil.createAndSendNewMessage(chatId, "Головне меню:", replyKeyboard);
    }

    private InlineKeyboardMarkup createMainMenuKeyboard(long chatId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        // first buttons row
        rows.add(KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("Реєстри сьогодні", "Type-->ScanSheetToday///Level-->1"),
                        KeyboardUtils.createInlineButton("Реєстри за 3 дні", "Type-->ScanSheet3Days///Level-->1")
                )
        );

        // second buttons row
        rows.add(KeyboardUtils.createInlineButtonRow(
                        KeyboardUtils.createInlineButton("Платне зберігання", "Type-->PaidStorage///Level-->1")
                )
        );

        // if user is admin -> also adding admin buttons row
        if (accessValidationService.isAdmin(chatId)) {
            rows.add(KeyboardUtils.createAdminButtonRow());
        }

        return KeyboardUtils.createInlineReplyMarkup(rows);
    }
}
