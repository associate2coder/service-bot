package ua.com.beautysmart.servicebot.domain.events.paidstorage;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class PaidStorageEvent extends ApplicationEvent {

    public PaidStorageEvent(Update source) {
        super(source);
    }
}
