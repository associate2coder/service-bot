package ua.com.beautysmart.servicebotbackend.domain.bot.exceptions;

public class TelegramApiRuntimeException extends RuntimeException{
    public TelegramApiRuntimeException(String message) {
        super(message);
    }
}
