package ua.com.beautysmart.servicebot.domain.events.mainmenu;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class MainMenuEvent extends ApplicationEvent {

    public MainMenuEvent(Update source) {
        super(source);
    }
}
