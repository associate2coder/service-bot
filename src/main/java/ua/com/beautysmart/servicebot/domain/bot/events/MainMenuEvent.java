package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MainMenuEvent extends ApplicationEvent {
    public MainMenuEvent(Update source) {
        super(source);
    }
}
