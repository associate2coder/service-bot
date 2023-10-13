package ua.com.beautysmart.servicebot.domain.exceptions;

/**
 * Author: associate2coder
 */

public class TelegramApiRuntimeException extends RuntimeException{

    public TelegramApiRuntimeException(String message) {
        super(message);
    }
}
