package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PaidStorage2DaysEvent extends ApplicationEvent {
    public PaidStorage2DaysEvent(Update source) {
        super(source);
    }
}
