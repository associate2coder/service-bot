package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class AddAdminEvent extends ApplicationEvent {

    public AddAdminEvent(Update source) {
        super(source);
    }
}
