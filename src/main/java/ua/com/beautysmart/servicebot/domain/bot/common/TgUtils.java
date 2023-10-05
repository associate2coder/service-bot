package ua.com.beautysmart.servicebot.domain.bot.common;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TgUtils {

    public static long getChatIdFromUpdate(Update update) {
        Message message = getMessageFromUpdate(update);
        return message.getChatId();
    }

    public static Message getMessageFromUpdate(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage()
                : update.getMessage();
    }
}
