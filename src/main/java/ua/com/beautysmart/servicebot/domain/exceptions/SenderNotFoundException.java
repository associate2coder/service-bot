package ua.com.beautysmart.servicebot.domain.exceptions;

/**
 * Author: associate2coder
 */

public class SenderNotFoundException extends RuntimeException{

    public SenderNotFoundException(String message) {
        super(message);
    }
}
