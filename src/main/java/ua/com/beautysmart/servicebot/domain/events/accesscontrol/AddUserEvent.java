package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class AddUserEvent extends ApplicationEvent {
    public AddUserEvent(Update source) {
        super(source);
    }
}
