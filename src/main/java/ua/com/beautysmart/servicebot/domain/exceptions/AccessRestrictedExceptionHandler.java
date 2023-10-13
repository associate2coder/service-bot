package ua.com.beautysmart.servicebot.domain.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ua.com.beautysmart.servicebot.domain.events.accesscontrol.AccessRestrictedEvent;

/**
 * Author: associate2coder
 */

@Aspect
@RequiredArgsConstructor
@Component
@Slf4j
public class AccessRestrictedExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @Pointcut("within(ua.com.beautysmart.servicebot.domain.services.*) && execution(* *(..))")
    public void matchAllMyMethods() {}

    @AfterThrowing(value = "matchAllMyMethods()", throwing = "exception")
    public void handleAccessRestrictedException(JoinPoint joinPoint, Throwable exception) {
        log.debug("Exception has been thrown: " + exception.getMessage());

        AccessRestrictedException e = (AccessRestrictedException) exception;
        log.warn("Attempt to access restricted resource. Exception message: " + e.getMessage());

        eventPublisher.publishEvent(new AccessRestrictedEvent(e.getUpdate()));
        log.debug("Access exception has been handled");
    }

}
