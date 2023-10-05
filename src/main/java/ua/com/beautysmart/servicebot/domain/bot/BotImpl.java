package ua.com.beautysmart.servicebot.domain.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuCommandHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotImpl extends TelegramLongPollingBot {

    @Value(value = "${bot-token}")
    private String TOKEN;

    @Value(value = "${bot-username}")
    private String USERNAME;

    private final MenuCommandHandler commandHandler;

    @PostConstruct
    public void init() {
        BotRegister botRegister = new BotRegister();
        try {
            botRegister.register(this);
        } catch (TelegramApiException e) {
            log.error("Error cause by exception with message: " + e.getMessage());
        }
    }

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
