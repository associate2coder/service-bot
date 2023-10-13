package ua.com.beautysmart.servicebot.domain.exceptions;

/**
 * Author: associate2coder
 */

public class ElementNotFoundException extends RuntimeException{

    public ElementNotFoundException(String message) {
        super(message);
    }
}
