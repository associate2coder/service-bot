package ua.com.beautysmart.servicebot.domain.bot.exceptions;

public class TelegramApiRuntimeException extends RuntimeException{
    public TelegramApiRuntimeException(String message) {
        super(message);
    }
}
