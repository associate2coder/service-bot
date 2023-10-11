package ua.com.beautysmart.servicebot.domain.bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage.PaidStorageTtnService;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage.TTN;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaidStorageEventListener {

    private final TelegramLongPollingBot bot;
    private final SenderService senderService;
    private final PaidStorageTtnService paidStorageService;

    @EventListener
    public void handlePaidStorage1DayEvent(PaidStorage1DayEvent event) {
        handlePaidStorageEvent(event, true);
    }

    @EventListener
    public void handlePaidStorage2DaysEvent(PaidStorage2DaysEvent event) {
        handlePaidStorageEvent(event, false);
    }

    private void handlePaidStorageEvent(ApplicationEvent event, boolean oneDay) {
        Update update = (Update) event.getSource();
        Map<Sender, List<TTN>> ttnsBySender = new HashMap<>();
        int days = oneDay ? 1 : 2;

        List<Sender> senders = senderService.getAllSenders();
        for (Sender sender: senders) {
            List<TTN> ttns = paidStorageService.getTtnForPaidStorage(sender, days);
            ttnsBySender.put(sender, ttns);
        }
        String text = getMessageTextFromMap(ttnsBySender, days);
        buildAndSendEditMessage(update, text);
    }

    private String getMessageTextFromMap(Map<Sender, List<TTN>> map, int days) {
        String dayText = days == 1 ? "1 день" : "1-2 дні";
        String resultText = "ЕН з платним зберіганням через " + dayText;
        if (ttnsNotFound(map)) {
            return resultText + " не знайдено";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(resultText + ":\n");
        for (Map.Entry<Sender, List<TTN>> entry: map.entrySet()) {
            List<TTN> list = entry.getValue();
            if (!list.isEmpty()) {
                sb.append(entry.getKey().getFullName() + ":\n");
                list.forEach(ttn -> sb.append("Замовлення: `" + ttn.getOrder() + "` ЕН: `" + ttn.getNumber() + "`\n"));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private boolean ttnsNotFound(Map<Sender, List<TTN>> map) {
        for (List<TTN> list: map.values()) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // TODO place all template messages to one class
    private void buildAndSendEditMessage(Update update, String text) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        int messageId = TgUtils.getMessageFromUpdate(update).getMessageId();
        InlineKeyboardMarkup replyMarkup = InlineKeyboardUtils.createReplyMarkup(InlineKeyboardUtils.createMainAsBackButtonRow());
        try {
            bot.execute(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .text(text)
                            .replyMarkup(replyMarkup)
                            .parseMode(ParseMode.MARKDOWN)
                            .build()
            );
            log.debug("Request to send message text to ScanSheet Info Menu has been sent to Telegram.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
