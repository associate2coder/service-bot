package ua.com.beautysmart.servicebot.domain.events.accesscontrol;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AccessRestrictedEvent extends ApplicationEvent {

    public AccessRestrictedEvent(Update source) {
        super(source);
    }
}
