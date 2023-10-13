package ua.com.beautysmart.servicebot.domain.bot.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Author: associate2coder
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageUtil {

    private final TelegramLongPollingBot bot;

    public void createAndSendNewMessage(long chatId, String text, ReplyKeyboard replyKeyboard, String parseMode) {
        sendMessageRequest(templateNewMessage(chatId, text, replyKeyboard, parseMode));
    }

    public void createAndSendNewMessage(long chatId, String text, ReplyKeyboard replyKeyboard) {
        sendMessageRequest(templateNewMessage(chatId, text, replyKeyboard, ParseMode.MARKDOWN));
    }

    public void createAndSendNewMessage(long chatId, String text) {
        sendMessageRequest(templateNewMessage(chatId, text, null, ParseMode.MARKDOWN));
    }

    public void createAndSendEditMessageText(Message message, String text, InlineKeyboardMarkup replyKeyboard, String parseMode) {
        sendMessageRequest(templateEditMessageText(message, text, replyKeyboard, parseMode));
    }

    public void createAndSendEditMessageText(Message message, String text, InlineKeyboardMarkup replyKeyboard) {
        sendMessageRequest(templateEditMessageText(message, text, replyKeyboard, ParseMode.MARKDOWN));
    }

    public void createAndSendEditMessageText(Message message, String text) {
        sendMessageRequest(templateEditMessageText(message, text, null, ParseMode.MARKDOWN));
    }

    public SendMessage templateNewMessage(long chatId, String text, ReplyKeyboard replyKeyboard, String parseMode) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode(parseMode) // Markdown, MarkdownV2, html
                .build();
    }

    public SendMessage templateNewMessage(long chatId, String text, ReplyKeyboard replyKeyboard) {
        return templateNewMessage(chatId, text, replyKeyboard, ParseMode.MARKDOWN);
    }

    public SendMessage templateNewMessage(long chatId, String text) {
        return templateNewMessage(chatId, text, null, ParseMode.MARKDOWN);
    }


    public EditMessageText templateEditMessageText(Message message, String text, InlineKeyboardMarkup replyKeyboard, String parseMode) {
        return EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode(parseMode) // Markdown, MarkdownV2, html
                .build();
    }

    public EditMessageText templateEditMessageText(Message message, String text, InlineKeyboardMarkup replyKeyboard) {
        return templateEditMessageText(message, text, replyKeyboard, ParseMode.MARKDOWN);
    }

    public EditMessageText templateEditMessageText(Message message, String text) {
        return templateEditMessageText(message, text, null, ParseMode.MARKDOWN);
    }

    public void sendMessageRequest(BotApiMethodMessage messageRequest) {
        log.debug("Message request has been received by the sender method and ready to be dispatched to Telegram API.");
        try {
            bot.execute(messageRequest);
        } catch (TelegramApiException e) {
            log.error("Unable to send requests to Telegram Api.");
        }
        log.debug("Message request has been dispatched to Telegram API without exceptions.");
    }

    public void sendMessageRequest(BotApiMethodSerializable messageEditRequest) {
        log.debug("Message edit request has been received by the sender method and ready to be dispatched to Telegram API.");
        try {
            bot.execute(messageEditRequest);
        } catch (TelegramApiException e) {
            log.error("Unable to send requests to Telegram Api.");
        }
        log.debug("Message edit request has been dispatched to Telegram API without exceptions.");
    }
}
