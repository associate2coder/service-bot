package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheet;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheetService;
import ua.com.beautysmart.servicebot.domain.services.AccessValidationService;
import ua.com.beautysmart.servicebot.domain.services.SenderService;
import ua.com.beautysmart.servicebot.domain.services.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetMenuEventListener {


    private final TelegramLongPollingBot bot;
    private final UserService userService;
    private final AccessValidationService accessValidationService;
    private final SenderService senderService;
    private final ScanSheetService scanSheetService;
    private final MenuContextHolder contextHolder;

    @EventListener
    public void handleScanSheetTodayMenuEvent(ScanSheetTodayMenuEvent event) {
        handleScanSheetMenuEvent(event, true);
    }

    @EventListener
    public void handleScanSheet3DaysMenuEvent(ScanSheetTodayMenuEvent event) {
        handleScanSheetMenuEvent(event, false);
    }

    public void handleScanSheetMenuEvent(ScanSheetTodayMenuEvent event, boolean today) {
        Update update = (Update) event.getSource();
        long chatId = update.getMessage().getChatId();

        Context context = contextHolder.getContext(chatId);

        // if user is not authorized, exception is thrown
        accessValidationService.validateUserAccess(update);


        Map<Sender, List<ScanSheet>> scansheets = retrieveScansheets(today);

        // creating keyboard for the Scansheet Menu
        ReplyKeyboard replyKeyboard = getReplyMarkup(scansheets, context);

        // sending ScanSheet Menu message to the user
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .text("Оберіть потрібний реєстр:")
                            .replyMarkup(replyKeyboard)
                            .build()
            );
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }

    private Map<Sender, List<ScanSheet>> retrieveScansheets(boolean today) {
        List<Sender> senders = senderService.getAllSenders();
        Map<Sender, List<ScanSheet>> scansheets = new HashMap<>();

        int days = today ? 0 : 2;

        for (Sender sender: senders) {
            List<ScanSheet> senderScansheets = scanSheetService.getScanSheets(sender, days);
            scansheets.put(sender, senderScansheets);
        }
        return scansheets;
    }
    private ReplyKeyboard getReplyMarkup(Map<Sender, List<ScanSheet>> mapScansheets, Context context) {

        List<ScanSheet> scansheets = getScansheetsFromMap(mapScansheets);

        // creating keyboard for the ScanSheet Menu
        List<List<InlineKeyboardButton>> listOfRows = new ArrayList<>();

        // iterating through scansheets and creating a button for each scansheet (to add it to its separate row)
        for (ScanSheet scansheet : scansheets) {

            // getting data for the button name and callback
            Sender sender = getSenderNameFromScanSheet(mapScansheets, scansheet);

            String buttonText = String.format(
                    "%s %s %s %s",
                    sender.getName(),
                    scansheet.getNumber(),
                    scansheet.getDateTime(),
                    scansheet.getCount()
            );

            String buttonCallbackData = String.format(
                    "MenuType-->%s///MenuLevel-->2///Value-->%s",
                    context.getMenuType(),
                    scansheet.getNumber()
            );

                    listOfRows.add(
                            List.of(
                                    InlineKeyboardUtils.createInlineButton(buttonText,buttonCallbackData)
                            )
                    );
        }
        return InlineKeyboardUtils.createReplyMarkup(listOfRows);
    }

    private List<ScanSheet> getScansheetsFromMap(Map<Sender, List<ScanSheet>> map) {
        List<ScanSheet> list = new ArrayList<>();
        for (List<ScanSheet> item: map.values()) {
            list.addAll(item);
        }
        Collections.sort(list);
        return list;
    }

    private Sender getSenderNameFromScanSheet(Map<Sender, List<ScanSheet>> scansheets, ScanSheet scanSheet) {
        for (Map.Entry<Sender, List<ScanSheet>> entry: scansheets.entrySet()) {
            if (entry.getValue().contains(scanSheet)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
