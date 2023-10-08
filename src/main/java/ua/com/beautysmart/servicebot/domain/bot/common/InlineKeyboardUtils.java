package ua.com.beautysmart.servicebot.domain.bot.common;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class InlineKeyboardUtils {

    public static InlineKeyboardButton createInlineButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(new String(text.getBytes(), StandardCharsets.UTF_8))
                .callbackData(callbackData)
                .build();
    }

    public static List<InlineKeyboardButton> createButtonRow(InlineKeyboardButton button1, InlineKeyboardButton button2) {
        return List.of(button1, button2);
    }

    public static List<InlineKeyboardButton> createButtonRow(InlineKeyboardButton button1) {
        return List.of(button1);
    }

    public static List<InlineKeyboardButton> createBackToMainButtonRow() {
        return List.of(
                InlineKeyboardUtils.createInlineButton("Головне меню", "Type-->MainMenu"),
                InlineKeyboardUtils.createInlineButton("Назад", "Type-->StepBack")
        );
    }

    public static List<InlineKeyboardButton> createMainAsBackButtonRow() {
        return List.of(
                InlineKeyboardUtils.createInlineButton("Назад до Головного меню", "Type-->MainMenu")
        );
    }

    public static List<InlineKeyboardButton> createAdminButtonRow() {
        return List.of(InlineKeyboardUtils.createInlineButton("Адміністрування", "Type-->AdminMenu///MenuLevel-->1")
        );
    }

    @SafeVarargs
    public static InlineKeyboardMarkup createReplyMarkup(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> listOfRows = Arrays.stream(rows).toList();
        return createReplyMarkup(listOfRows);

    }

    public static InlineKeyboardMarkup createReplyMarkup(List<List<InlineKeyboardButton>> listOfRows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(listOfRows)
                .build();
    }

}
