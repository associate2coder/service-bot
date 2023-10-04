package ua.com.beautysmart.servicebot.domain.bot.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ScanSheetInfoEvent extends ApplicationEvent {
    public ScanSheetInfoEvent(Update source) {
        super(source);
    }
}
