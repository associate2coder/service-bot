package ua.com.beautysmart.servicebot.domain.bot.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Author: associate2coder
 */

@Component
@RequiredArgsConstructor
public class MenuContextHolder {

    private final Map<Long, Context> storage;

    public void addContext(Context context) {
        storage.put(context.getChatId(), context);
    }

    public Context getContext(long chatId) {
        return storage.get(chatId);
    }
}
