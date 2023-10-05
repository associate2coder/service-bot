package ua.com.beautysmart.servicebot.domain.bot.menu;

import lombok.*;
import org.hibernate.sql.Update;
import ua.com.beautysmart.servicebot.domain.entities.Sender;
import ua.com.beautysmart.servicebot.domain.entities.User;
import ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo.SenderRequest;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<Integer, String> menuHistory;
    @Getter @Setter
    private SenderRequest addedSender;
    @Getter @Setter
    private User addedUser;

    public Context(long chatId) {
        this.chatId = chatId;
        this.menuHistory = new HashMap<>();
        // Context is being created / re-created when MainMenu (or /start) commands are called
        this.menuType = "MainMenu";
        this.menuLevel = 0;
    }

    public void addMenuHistoryItem(int menuLevel, String returnCallbackData) {
        menuHistory.put(menuLevel, returnCallbackData);
    }

    public String getMenuHistoryItem(int menuLevel) {
        return menuHistory.get(menuLevel);
    }

    public void clear() {
        menuHistory.clear();
        clearAddedSender();
    }

    public void clearAddedSender() {
        addedSender = null;
        addedUser = null;
    }
}
