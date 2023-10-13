package ua.com.beautysmart.servicebot.domain.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.com.beautysmart.servicebot.domain.services.AccessRequestService;

import java.util.List;

/**
 * Author: associate2coder
 */

// TODO update exception handler

@Aspect
@RequiredArgsConstructor
@Component
@Slf4j
public class ExceptionsHandler {

    private final TelegramLongPollingBot bot;
    private final AccessRequestService requestService;



    public void handleTelegramApiRuntimeException(TelegramApiRuntimeException e, WebRequest request) {
        log.error("Unable to send message to telegram due to an error. Exception message: " + e.getMessage());
    }

    public void handleSenderNotFoundException(SenderNotFoundException e, WebRequest request) {

        // TODO add handler which sends telegram message
        log.error("Error. Exception message: " + e.getMessage());
    }

    // TODO CustomRuntimeException handler
}
