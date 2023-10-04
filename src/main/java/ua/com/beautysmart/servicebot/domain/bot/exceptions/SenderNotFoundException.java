package ua.com.beautysmart.servicebot.domain.bot.exceptions;

public class SenderNotFoundException extends RuntimeException{

    public SenderNotFoundException(String message) {
        super(message);
    }
}
