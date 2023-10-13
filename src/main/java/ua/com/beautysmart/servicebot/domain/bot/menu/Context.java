package ua.com.beautysmart.servicebot.domain.bot.menu;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.com.beautysmart.servicebot.domain.entities.User;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: associate2coder
 */

@EqualsAndHashCode
@ToString
public class Context {

    @Getter
    private final long chatId;
    @Getter @Setter
    private String menuType;
    @Getter @Setter
    private int menuLevel;
    @Getter @Setter
    private String value;
    private final Map<Integer, Update> history;
    @Getter @Setter
    private SenderRequest addedSender;
    @Getter @Setter
    private User addedUser;
    @Getter @Setter
    private boolean back;

    public Context(long chatId) {
        this.chatId = chatId;
        this.history = new HashMap<>();
        // Context is being created / re-created when MainMenu (or /start) commands are called
        this.menuType = "MainMenu";
        this.menuLevel = 0;
        this.back = false;
    }

    public void addMenuHistoryItem(int menuLevel, Update returnUpdate) {
        history.put(menuLevel, returnUpdate);
    }

    public Update getMenuHistoryItem(int menuLevel) {
        return history.get(menuLevel);
    }

    public void clear() {
        clearAddedSender();
        clearAddedUser();
    }

    public void clearAddedSender() {
        addedSender = null;
    }

    public void clearAddedUser() {
        addedUser = null;
    }

    public void updateHistoryLevel() {
        history.entrySet().removeIf(entry -> entry.getKey() > menuLevel);
    }
}
