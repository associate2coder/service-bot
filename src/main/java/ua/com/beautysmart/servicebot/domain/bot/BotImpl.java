package ua.com.beautysmart.servicebot.domain.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuCommandHandler;

@Component
@Slf4j
public class BotImpl extends TelegramLongPollingBot {

    @Value(value = "${bot-username}")
    private String username;

    private final MenuCommandHandler commandHandler;

    public BotImpl(String botToken, MenuCommandHandler commandHandler) {
        super(botToken);
        this.commandHandler = commandHandler;
    }

    @PostConstruct
    public void init() {
        BotRegister botRegister = new BotRegister();
        try {
            botRegister.register(this);
        } catch (TelegramApiException e) {
            log.error("Error caused by exception with message: " + e.getMessage());
        }
        log.info("Telegram bot is loaded and running");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null) {

            commandHandler.handle(update);
            log.debug("Update with id " + update.getUpdateId() + " has been handled.");
        } else {
            log.warn("No update has been received from telegram bot.");
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
