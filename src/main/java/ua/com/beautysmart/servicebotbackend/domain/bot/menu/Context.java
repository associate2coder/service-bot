package ua.com.beautysmart.servicebotbackend.domain.bot.menu;

import lombok.*;
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
}
