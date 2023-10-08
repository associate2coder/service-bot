package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheet;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheetService;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetMenuEventListener {


    private final TelegramLongPollingBot bot;
    private final AccessValidationService accessValidationService;
    private final SenderService senderService;
    private final ScanSheetService scanSheetService;
    private final MenuContextHolder contextHolder;

    @EventListener
    public void handleScanSheetTodayMenuEvent(ScanSheetTodayMenuEvent event) {
        handleScanSheetMenuEvent(event, true);
    }

    @EventListener
    public void handleScanSheet3DaysMenuEvent(ScanSheet3DaysMenuEvent event) {
        handleScanSheetMenuEvent(event, false);
    }

    public void handleScanSheetMenuEvent(ApplicationEvent event, boolean today) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        Context context = contextHolder.getContext(chatId);

        // if user is not authorized, exception is thrown
        accessValidationService.validateUserAccess(update);


        Map<Sender, List<ScanSheet>> scanSheets = retrieveScanSheets(today);
        log.debug("Total number of scanSheets found as per search parameters: " + scanSheets.size());

        // creating keyboard for the ScanSheet Menu
        InlineKeyboardMarkup replyMarkup = getReplyMarkup(scanSheets, context);

        // sending ScanSheet Menu message to the user
        String text = scanSheetsNotFound(scanSheets) ? "Реєстрів не знайдено." : "Оберіть потрібний реєстр:";
        buildAndSendEditMessage(update, text, replyMarkup);

    }

    private Map<Sender, List<ScanSheet>> retrieveScanSheets(boolean today) {
        List<Sender> senders = senderService.getAllSenders();
        Map<Sender, List<ScanSheet>> scanSheets = new HashMap<>();

        int days = today ? 0 : 2;
        log.debug("ScanSheets will be retrieved for " + (today ? "today only." : "three recent days including today"));

        for (Sender sender: senders) {
            List<ScanSheet> senderScanSheets = scanSheetService.getScanSheets(sender, days);
            scanSheets.put(sender, senderScanSheets);
        }
        return scanSheets;
    }
    private InlineKeyboardMarkup getReplyMarkup(Map<Sender, List<ScanSheet>> mapScanSheets, Context context) {

        List<ScanSheet> scanSheets = getScanSheetsFromMap(mapScanSheets);

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
                                    InlineKeyboardUtils.createInlineButton(buttonText,buttonCallbackData)
                            )
                    );
        }
        listOfRows.add(InlineKeyboardUtils.createMainAsBackButtonRow());
        return InlineKeyboardUtils.createReplyMarkup(listOfRows);
    }

    private List<ScanSheet> getScanSheetsFromMap(Map<Sender, List<ScanSheet>> map) {
        List<ScanSheet> list = new ArrayList<>();
        for (List<ScanSheet> item: map.values()) {
            list.addAll(item);
        }
        list.sort(Comparator.reverseOrder());
        return list;
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

    private void buildAndSendEditMessage(Update update, String text, InlineKeyboardMarkup replyMarkup) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        int messageId = TgUtils.getMessageFromUpdate(update).getMessageId();
        try {
            bot.execute(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .text(text)
                            .replyMarkup(replyMarkup)
                            .build()
            );
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
