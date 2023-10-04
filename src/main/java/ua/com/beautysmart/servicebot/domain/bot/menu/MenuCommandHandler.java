package ua.com.beautysmart.servicebot.domain.bot.menu;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MenuCommandHandler {

    void handle(Update update);

}
