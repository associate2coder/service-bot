package ua.com.beautysmart.servicebotbackend.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ScanSheetTodayMenuEvent extends ApplicationEvent {
    public ScanSheetTodayMenuEvent(Update source) {
        super(source);
    }
}
