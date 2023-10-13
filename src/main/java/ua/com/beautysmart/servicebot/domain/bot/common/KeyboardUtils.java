package ua.com.beautysmart.servicebot.domain.bot.common;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Author: associate2coder
 */

public class KeyboardUtils {

    public static InlineKeyboardButton createInlineButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(new String(text.getBytes(), StandardCharsets.UTF_8))
                .callbackData(callbackData)
                .build();
    }

    public static List<InlineKeyboardButton> createInlineButtonRow(InlineKeyboardButton button1, InlineKeyboardButton button2) {
        return List.of(button1, button2);
    }

    public static List<InlineKeyboardButton> createInlineButtonRow(InlineKeyboardButton button1) {
        return List.of(button1);
    }

    public static List<InlineKeyboardButton> createBackToMainInlineButtonRow() {
        return List.of(
                KeyboardUtils.createInlineButton("Головне меню", "Type-->MainMenu"),
                KeyboardUtils.createInlineButton("Назад", "Type-->StepBack")
        );
    }

    public static List<InlineKeyboardButton> createMainAsBackInlineButtonRow() {
        return List.of(
                KeyboardUtils.createInlineButton("Назад до Головного меню", "Type-->MainMenu")
        );
    }


    public static List<InlineKeyboardButton> createAdminButtonRow() {
        return List.of(KeyboardUtils.createInlineButton("Адміністрування", "Type-->AdminMenu///MenuLevel-->1")
        );
    }

    @SafeVarargs
    public static InlineKeyboardMarkup createInlineReplyMarkup(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> listOfRows = Arrays.stream(rows).toList();
        return createInlineReplyMarkup(listOfRows);

    }

    public static InlineKeyboardMarkup createInlineReplyMarkup(List<List<InlineKeyboardButton>> listOfRows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(listOfRows)
                .build();
    }
}
