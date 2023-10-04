package ua.com.beautysmart.servicebot.domain.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuCommandHandler;

@Component
@PropertySource("classpath:secrets.properties")
@RequiredArgsConstructor
public class BotImpl extends TelegramLongPollingBot {

    @Value("${bot-token}")
    private static String TOKEN;

    @Value("${bot-username}")
    private static String USERNAME;

    private final MenuCommandHandler commandHandler;

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null) {
            commandHandler.handle(update);
        }
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }
}
