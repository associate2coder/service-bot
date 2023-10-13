package ua.com.beautysmart.servicebot.domain.events.scansheets;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: associate2coder
 */

public class ScanSheet3DaysMenuEvent extends ApplicationEvent {

    public ScanSheet3DaysMenuEvent(Update source) {
        super(source);
    }
}
