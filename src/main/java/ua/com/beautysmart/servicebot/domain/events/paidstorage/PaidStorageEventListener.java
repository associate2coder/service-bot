package ua.com.beautysmart.servicebot.domain.events.paidstorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage.PaidStorageTtnService;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage.TTN;
import ua.com.beautysmart.servicebot.domain.services.SenderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PaidStorageEventListener {

    private final SenderService senderService;
    private final PaidStorageTtnService paidStorageService;
    private final MessageUtil messageUtil;

    @EventListener
    public void handlePaidStorageEvent(PaidStorageEvent event) {
        // retrieve update from event
        Update update = (Update) event.getSource();

        // empty map to be populated by TTN searches later on
        Map<Sender, List<TTN>> ttnsBySender = new HashMap<>();

        // retrieve all senders
        List<Sender> senders = senderService.getAllSenders();

        for (Sender sender: senders) {
            List<TTN> ttns = paidStorageService.getTtnForPaidStorage(sender);
            ttnsBySender.put(sender, ttns);
        }

        String text = getMessageTextFromMap(ttnsBySender);
        Message message = TgUtils.getMessageFromUpdate(update);
        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(KeyboardUtils.createMainAsBackInlineButtonRow());
        messageUtil.createAndSendEditMessageText(message, text, replyKeyboard, ParseMode.MARKDOWN);
    }

    private String getMessageTextFromMap(Map<Sender, List<TTN>> map) {
        StringBuilder sb = new StringBuilder();

        // first line - introductory header
        sb.append("ЕН з платним зберіганням, в тому числі яке починається через 1 день")
                .append(ttnsNotFound(map) ? " не знайдено." : ":\n");

        // template string to be formatted below
        String line = "Замовлення: `%s` ЕН: `%s`%n";

        // iterate through each sender
        for (Map.Entry<Sender, List<TTN>> entry: map.entrySet()) {
            // iterate through list of TTNs of the relevant sender
            List<TTN> list = entry.getValue();
            // if list is not empty, the content is printed according to template
            if (!list.isEmpty()) {
                // sender full name is printed first
                sb.append(entry.getKey().getFullName()).append(":\n");
                // line for each TTN
                list.forEach(ttn -> sb.append(String.format(line, ttn.getOrder(), ttn.getNumber())));
                // whitespace line to add space between TTNs of different senders (if more than one)
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
}
