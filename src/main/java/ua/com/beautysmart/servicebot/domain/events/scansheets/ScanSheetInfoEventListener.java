package ua.com.beautysmart.servicebot.domain.events.scansheets;

import com.pnuema.java.barcode.Barcode;
import com.pnuema.java.barcode.EncodingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.bot.common.KeyboardUtils;
import ua.com.beautysmart.servicebot.domain.bot.common.MessageUtil;
import ua.com.beautysmart.servicebot.domain.bot.common.TgUtils;
import ua.com.beautysmart.servicebot.domain.exceptions.CustomRuntimeException;
import ua.com.beautysmart.servicebot.domain.exceptions.TelegramApiRuntimeException;
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

/**
 * Author: associate2coder
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanSheetInfoEventListener {

    private final TelegramLongPollingBot bot;
    private final MenuContextHolder contextHolder;
    private final ScanSheetUtils scanSheetUtils;
    private final Barcode barcode;
    private final MessageUtil messageUtil;

    @EventListener
    public void handleScanSheetInfoEvent(ScanSheetInfoEvent event) {

        // current update (and relevant user chat id) is retrieved
        Update update = (Update) event.getSource();
        long chatId = TgUtils.getChatIdFromUpdate(update);

        // context is retrieved by chat id
        Context context = contextHolder.getContext(chatId);
        // check whether scanSheet is within 1-day branch or 3-day branch
        boolean today = "ScanSheetToday".equals(context.getMenuType());

        // retrieve information on the scansheet and on the underlying sender
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

        Message message = TgUtils.getMessageFromUpdate(update);
        messageUtil.createAndSendEditMessageText(message, text1);

        // TODO add send image template
        buildAndSendImage(chatId, scanSheet.getNumber());

        String text2 = "Штрих-код cформовано";
        InlineKeyboardMarkup replyKeyboard = KeyboardUtils.createInlineReplyMarkup(KeyboardUtils.createBackToMainInlineButtonRow());
        messageUtil.createAndSendNewMessage(chatId, text2, replyKeyboard);
    }

    // TODO create a separate template

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
}
