package ua.com.beautysmart.servicebot.domain.bot.exceptions;

public class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException(String message) {
        super(message);
    }
}
