package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminMenuEvent extends ApplicationEvent {
    public AdminMenuEvent(Update source) {
        super(source);
    }
}
