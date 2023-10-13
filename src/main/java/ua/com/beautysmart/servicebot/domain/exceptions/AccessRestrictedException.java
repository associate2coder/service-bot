package ua.com.beautysmart.servicebot.domain.exceptions;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class AccessRestrictedException extends RuntimeException{

    @Getter
    private final Update update;

    public AccessRestrictedException(String message, Update update) {
        super(message);
        this.update = update;
    }
}
