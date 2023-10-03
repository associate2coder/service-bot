package ua.com.beautysmart.servicebotbackend.domain.bot.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebotbackend.domain.bot.events.MainMenuEvent;
import ua.com.beautysmart.servicebotbackend.domain.bot.events.ScanSheetInfoEvent;
import ua.com.beautysmart.servicebotbackend.domain.bot.events.ScanSheetTodayMenuEvent;
import ua.com.beautysmart.servicebotbackend.domain.services.AccessValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuCommandHandlerImpl implements MenuCommandHandler {

    private final MenuContextHolder contextHolder; // TODO check whether this is needed here
    private final AccessValidationService accessValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handle(Update update) {

        // update is handled only when it has a message (i.e. we react on user message)
        if (update.hasMessage()) {

            Message message = update.getMessage();
            long chatId = message.getChatId();

            // if chatId is not in User database, access is not granted
            accessValidationService.validateUserAccess(update);

            // first it is checked whether message is a command.
            // Commands are handled with Command handler method below.
            String messageText = message.getText();
            if (messageText.startsWith("/")) {
                handleCommands(update);
                return;
            }

            // if message is not a command, it is checked whether the message has any callbackData
            // CallbackData is checked with a CallbackData handler method below.
            if (update.getCallbackQuery() != null) {
                handleCallbackData(update);
                return;
            }
        }
    }

    private void handleCommands(Update update) {

        switch (update.getMessage().getText()) {
            case "/start" -> eventPublisher.publishEvent(new MainMenuEvent(update));
        }

    }
    private void handleCallbackData(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getMessage().getChatId();
        Context context = updateContextWithCallbackData(callbackData, chatId);

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
