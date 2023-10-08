package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PaidStorage1DayEvent extends ApplicationEvent {
    public PaidStorage1DayEvent(Update source) {
        super(source);
    }
}
