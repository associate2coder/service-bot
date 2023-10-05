package ua.com.beautysmart.servicebot.domain.bot.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.events.*;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuCommandHandlerImpl implements MenuCommandHandler {

    private final MenuContextHolder contextHolder; // TODO check whether this is needed here
    private final AccessValidationService accessValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handle(Update update) {

        // if chatId is not in User database, access is not granted
        accessValidationService.validateUserAccess(update);

        // if message has a callback query, callback query is handled
        if (update.hasCallbackQuery()) {
            log.debug("Update has a callback query with call back data: " + update.getCallbackQuery().getData());
            handleCallbackData(update);
            return;
        }


        // if message does not have a callback query but has a message (e.g., /start or other command)
        // such update with command is handled accordingly
        if (update.hasMessage()) {

            log.debug("Update has a message with text: " + update.getMessage().getText());
            Message message = update.getMessage();

            // first it is checked whether message is a command.
            // Commands are handled with Command handler method below.
            String messageText = message.getText();
            if (messageText.startsWith("/")) {
                handleCommands(update);
                return;
            }

            long chatId = TgUtils.getChatIdFromUpdate(update);
            Context context = contextHolder.getContext(chatId);
            if ("AddSender".equals(context.getMenuType())) {
                eventPublisher.publishEvent(new AddSenderMenuEvent(update));
                return;
            }

        }
    }

    private void handleCommands(Update update) {

        // switch is introduced since it is not expected that there will be lots of commands
        // additional commands are under consideration
        switch (update.getMessage().getText()) {
            case "/start" -> eventPublisher.publishEvent(new MainMenuEvent(update));
        }
    }
    private void handleCallbackData(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        long chatId = TgUtils.getChatIdFromUpdate(update);
        Context context = updateContextWithCallbackData(callbackData, chatId);
        log.debug("Context: " + context);

        switch (context.getMenuType()) {
            case "MainMenu" -> eventPublisher.publishEvent(new MainMenuEvent(update));
            case "ScanSheetToday" -> {
                if (context.getMenuLevel() == 1) {
                    eventPublisher.publishEvent(new ScanSheetTodayMenuEvent(update));
                } else if (context.getMenuLevel() == 2) {
                    eventPublisher.publishEvent(new ScanSheetInfoEvent(update));
                } else {
                    log.error("ScanSheetToday MenuType appears to have menu levels other than expected.");
                }
            }
            case "PaidStorage" -> {

            }
            case "AdminMenu" -> eventPublisher.publishEvent(new AdminMenuEvent(update));
        }
    }

    private Context updateContextWithCallbackData(String callbackData, long chatId) {
        Context context = contextHolder.getContext(chatId);

        String[] argGroups = callbackData.split("///");

        for (String arg: argGroups) {
            String[] str = arg.split("-->");

            // updating context with new data
            switch(str[0]) {
                case "MenuType" -> context.setMenuType(str[1]);
                case "MenuLevel" -> context.setMenuLevel(Integer.parseInt(str[1]));
                case "Value" -> context.setValue(str[1]);
            }
        }
        // adding existing state into context
        context.addMenuHistoryItem(context.getMenuLevel(), callbackData);
        return context;
    }
}
