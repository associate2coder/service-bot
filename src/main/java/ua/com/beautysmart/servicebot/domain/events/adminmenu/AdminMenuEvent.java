package ua.com.beautysmart.servicebot.domain.events.adminmenu;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class AdminMenuEvent extends ApplicationEvent {
    public AdminMenuEvent(Update source) {
        super(source);
    }
}
