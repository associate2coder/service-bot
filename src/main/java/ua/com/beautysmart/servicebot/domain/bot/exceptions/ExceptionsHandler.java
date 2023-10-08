package ua.com.beautysmart.servicebot.domain.bot.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ControllerAdvice
@RequiredArgsConstructor
@Component
@Slf4j
public class ExceptionsHandler {

    private final TelegramLongPollingBot bot;

    @ExceptionHandler(AccessRestrictedException.class)
    public void handleBadRequestException(AccessRestrictedException e, WebRequest request) {

        log.warn("Attempt to access restricted resource. Exception message: " + e.getMessage());

        try {
            bot.execute(SendMessage.builder()
                    .chatId(e.getChatId())
                    .text(e.getMessage())
                    .build()
            );
        } catch (TelegramApiException exception) {
            throw new TelegramApiRuntimeException(exception.getMessage());
        }
    }

    @ExceptionHandler(TelegramApiRuntimeException.class)
    public void handleTelegramApiRuntimeException(TelegramApiRuntimeException e, WebRequest request) {
        log.error("Unable to send message to telegram due to an error. Exception message: " + e.getMessage());
    }

    @ExceptionHandler(SenderNotFoundException.class)
    public void handleSenderNotFoundException(SenderNotFoundException e, WebRequest request) {

        // TODO add handler which sends telegram message
        log.error("Error. Exception message: " + e.getMessage());
    }

    // TODO CustomRuntimeException handler
}
