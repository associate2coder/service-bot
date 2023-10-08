package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ScanSheet3DaysMenuEvent extends ApplicationEvent {
    public ScanSheet3DaysMenuEvent(Update source) {
        super(source);
    }
}
