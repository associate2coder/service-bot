package ua.com.beautysmart.servicebot.domain.bot.events;

import com.pnuema.java.barcode.Barcode;
import com.pnuema.java.barcode.EncodingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.InlineKeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.CustomRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.exceptions.TelegramApiRuntimeException;
import ua.com.beautysmart.servicebot.domain.bot.menu.Context;
import ua.com.beautysmart.servicebot.domain.bot.menu.MenuContextHolder;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheet;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets.ScanSheetUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetInfoEventListener {

    private final TelegramLongPollingBot bot;
    private final MenuContextHolder contextHolder;
    private final ScanSheetUtils scanSheetUtils;
    private final Barcode barcode;

    @EventListener
    public void handleScanSheetInfoEvent(ScanSheetInfoEvent event) {
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);
        Context context = contextHolder.getContext(chatId);
        boolean today = "ScanSheetToday".equals(context.getMenuType());

        Pair<Sender, ScanSheet> pair = scanSheetUtils.getSenderAndScanSheetByNumber(today, context.getValue());
        Sender sender = pair.getFirst();
        ScanSheet scanSheet = pair.getSecond();

        String text1 = String.format("""
                %s
                Номер реєстру: `%s`
                Дата створення: %s
                Кількість ЕН: %s шт.
                Штрих-код:""",
                sender.getFullName(),
                scanSheet.getNumber(),
                scanSheet.getDateTime(),
                scanSheet.getCount());

        String text2 = "Штрих-код формовано";

        InlineKeyboardMarkup replyMarkup = InlineKeyboardUtils.createReplyMarkup(InlineKeyboardUtils.createBackToMainButtonRow());

        buildAndSendEditMessage(update, text1);
        buildAndSendImage(chatId, scanSheet.getNumber());
        buildAndSendMessage(chatId, text2, replyMarkup);
    }

    private void buildAndSendEditMessage(Update update, String text) {
        long chatId = TgUtils.getChatIdFromUpdate(update);
        int messageId = TgUtils.getMessageFromUpdate(update).getMessageId();
        try {
            bot.execute(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .text(text)
                            .parseMode(ParseMode.MARKDOWN)
                            .build()
            );
            log.debug("Request to send message text to ScanSheet Info Menu has been sent to Telegram.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }

    public void buildAndSendImage(long chatId, String number) {
        try {
            Image img = barcode.encode(EncodingType.CODE128, number);
            File barcodeImageFile = new File("image.png");
            ImageIO.write((RenderedImage) img, "png", barcodeImageFile);
            InputFile inputFile = new InputFile(barcodeImageFile);

            bot.execute(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(inputFile)
                            .build()
            );
            log.debug("Request to send barcode as image to ScanSheet Info Menu has been sent to Telegram.");
            barcodeImageFile.delete();
        } catch (IOException e) {
            throw new CustomRuntimeException("Unexpected exception during sending barcode as an image.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }

    private void buildAndSendMessage(long chatId, String text, InlineKeyboardMarkup replyMarkup) {
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .replyMarkup(replyMarkup)
                            .build()
            );
            log.debug("Request to send message text to ScanSheet Info Menu has been sent to Telegram.");
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e.getMessage());
        }
    }
}
