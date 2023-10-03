package ua.com.beautysmart.servicebotbackend.domain.bot.exceptions;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AccessRestrictedException extends RuntimeException{

    @Getter
    private final long chatId;
    public AccessRestrictedException(String message, Update update) {
        super(message);
        this.chatId = update.getMessage().getChatId();
    }
}
