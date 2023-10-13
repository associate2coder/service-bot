package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class RequestAccessEvent extends ApplicationEvent {

    public RequestAccessEvent(Update source) {
        super(source);
    }
}
