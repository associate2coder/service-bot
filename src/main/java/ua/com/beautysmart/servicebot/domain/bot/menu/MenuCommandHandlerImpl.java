package ua.com.beautysmart.servicebot.domain.bot.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.events.accesscontrol.RequestAccessEvent;
import ua.com.beautysmart.servicebot.domain.events.adminmenu.AddSenderMenuEvent;
import ua.com.beautysmart.servicebot.domain.events.accesscontrol.AddUserConfirmedEvent;
import ua.com.beautysmart.servicebot.domain.events.accesscontrol.AddUserEvent;
import ua.com.beautysmart.servicebot.domain.events.adminmenu.AdminMenuEvent;
import ua.com.beautysmart.servicebot.domain.events.mainmenu.MainMenuEvent;
import ua.com.beautysmart.servicebot.domain.events.paidstorage.PaidStorageEvent;
import ua.com.beautysmart.servicebot.domain.events.scansheets.ScanSheet3DaysMenuEvent;
import ua.com.beautysmart.servicebot.domain.events.scansheets.ScanSheetInfoEvent;
import ua.com.beautysmart.servicebot.domain.events.scansheets.ScanSheetTodayMenuEvent;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuCommandHandlerImpl implements MenuCommandHandler {

    private final MenuContextHolder contextHolder; // TODO check whether this is needed here
    private final AccessValidationService accessValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handle(Update update) {

        long chatId = TgUtils.getChatIdFromUpdate(update);

        // if chatId is not in User database, access is not granted
        if (!accessValidationService.isUser(chatId)) {
            eventPublisher.publishEvent(new RequestAccessEvent(update));
            return;
        }

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

        long chatId = TgUtils.getChatIdFromUpdate(update);
        Context context = updateContextWithCallbackData(update, chatId);
        log.debug("Context: " + context);

        switch (context.getMenuType()) {
            case "MainMenu" -> eventPublisher.publishEvent(new MainMenuEvent(update));
            case "StepBack" -> {
                context.setBack(true);
                int previousMenu = context.getMenuLevel() - 1;
                Update returnUpdate = context.getMenuHistoryItem(previousMenu);
                handleCallbackData(returnUpdate);
                context.setBack(false);
            }
            case "ScanSheetToday" -> {
                switch (context.getMenuLevel()) {
                    case 1 -> eventPublisher.publishEvent(new ScanSheetTodayMenuEvent(update)); // choose scanSheet
                    case 2 -> eventPublisher.publishEvent(new ScanSheetInfoEvent(update)); // see scanSheet info
                    default -> log.error("ScanSheetToday MenuType appears to have menu levels other than expected."); // TODO: add exception and its handler
                }
            }
            case "ScanSheet3Days" -> {
                switch (context.getMenuLevel()) {
                    case 1 -> eventPublisher.publishEvent(new ScanSheet3DaysMenuEvent(update)); // choose scanSheet
                    case 2 -> eventPublisher.publishEvent(new ScanSheetInfoEvent(update)); // see scanSheet info
                    default -> log.error("ScanSheet3Days MenuType appears to have menu levels other than expected."); // TODO: add exception and its handler
                }
            }
            case "PaidStorage" -> eventPublisher.publishEvent(new PaidStorageEvent(update));
            case "AdminMenu" -> eventPublisher.publishEvent(new AdminMenuEvent(update));
            case "AddSender" -> eventPublisher.publishEvent(new AddSenderMenuEvent(update));
            case "AddUser" -> eventPublisher.publishEvent(new AddUserEvent(update));
            case "AddUserConfirmed" -> eventPublisher.publishEvent(new AddUserConfirmedEvent(update));
        }
    }



    private Context updateContextWithCallbackData(Update update, long chatId) {
        String callbackData = update.getCallbackQuery().getData();

        Context context = contextHolder.getContext(chatId);

        String[] argGroups = callbackData.split("///");

        for (String arg: argGroups) {
            String[] str = arg.split("-->");

            // updating context with new data
            switch(str[0]) {
                case "Type" -> context.setMenuType(str[1]);
                case "Level" -> {
                    context.setMenuLevel(Integer.parseInt(str[1]));
                    context.updateHistoryLevel();
                }
                case "Value" -> context.setValue(str[1]);
            }
        }
        // adding existing state into context
        context.addMenuHistoryItem(context.getMenuLevel(), update);
        return context;
    }
}
