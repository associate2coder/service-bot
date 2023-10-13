package ua.com.beautysmart.servicebot.domain.events.scansheets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheet;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheetUtils;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;

import java.util.*;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetMenuEventListener {

    private final AccessValidationService accessValidationService;
    private final ScanSheetUtils scanSheetUtils;
    private final MenuContextHolder contextHolder;
    private final MessageUtil messageUtil;

    @EventListener
    public void handleScanSheetTodayMenuEvent(ScanSheetTodayMenuEvent event) {
        handleScanSheetMenuEvent(event, true);
    }

    @EventListener
    public void handleScanSheet3DaysMenuEvent(ScanSheet3DaysMenuEvent event) {
        handleScanSheetMenuEvent(event, false);
    }

    public void handleScanSheetMenuEvent(ApplicationEvent event, boolean today) {
        // retrieve update and chat id
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        // retrieve context
        Context context = contextHolder.getContext(chatId);

        // if user is not authorized, exception is thrown
        accessValidationService.validateUserAccess(update);

        // retrieve scanSheets (true - today only, false - today and 2 previous days)
        Map<Sender, List<ScanSheet>> scanSheets = scanSheetUtils.retrieveScanSheets(today);
        log.debug("Total number of scanSheets found as per search parameters: " + scanSheets.size());

        // creating keyboard for the ScanSheet Menu
        InlineKeyboardMarkup replyKeyboard = createReplyKeyboard(scanSheets, context);

        // sending ScanSheet Menu message to the user
        String text = scanSheetsNotFound(scanSheets) ? "Реєстрів не знайдено." : "Оберіть потрібний реєстр:";

        if (context.isBack()) {
            // if context indicates that menu is printed after "Back" button, new message is sent
            messageUtil.createAndSendNewMessage(chatId, text, replyKeyboard);
        } else {
            // if menu is called initially (not by pressing "Back" button), existing message is edited
            Message message = TgUtils.getMessageFromUpdate(update);
            messageUtil.createAndSendEditMessageText(message, text, replyKeyboard);
        }
    }


    private InlineKeyboardMarkup createReplyKeyboard(Map<Sender, List<ScanSheet>> mapScanSheets, Context context) {

        List<ScanSheet> scanSheets = scanSheetUtils.getScanSheetsFromMap(mapScanSheets);

        // creating keyboard for the ScanSheet Menu
        List<List<InlineKeyboardButton>> listOfRows = new ArrayList<>();

        // iterating through scanSheets and creating a button for each scanSheet (to add it to its separate row)
        for (ScanSheet scansheet : scanSheets) {

            // getting data for the button name and callback
            Sender sender = getSenderNameFromScanSheet(mapScanSheets, scansheet);

            String buttonText = String.format(
                    "%s %s %s %s",
                    sender.getAlias(),
                    scansheet.getNumber(),
                    scansheet.getDateTime(),
                    scansheet.getCount()
            );

            String buttonCallbackData = String.format(
                    "Type-->%s///Level-->2///Value-->%s",
                    context.getMenuType(),
                    scansheet.getNumber()
            );

                    listOfRows.add(
                            List.of(
                                    KeyboardUtils.createInlineButton(buttonText,buttonCallbackData)
                            )
                    );
        }
        listOfRows.add(KeyboardUtils.createMainAsBackInlineButtonRow());
        return KeyboardUtils.createInlineReplyMarkup(listOfRows);
    }

    private boolean scanSheetsNotFound(Map<Sender, List<ScanSheet>> map) {
        for (List<ScanSheet> item: map.values()) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Sender getSenderNameFromScanSheet(Map<Sender, List<ScanSheet>> scanSheets, ScanSheet scanSheet) {
        for (Map.Entry<Sender, List<ScanSheet>> entry: scanSheets.entrySet()) {
            if (entry.getValue().contains(scanSheet)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
