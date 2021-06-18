package chat;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chat {
    private final Map<User, PrintWriter> usersList = new ConcurrentHashMap<>();

    private static Chat chat;

    private Chat() {}

    protected static Chat getInstance() {
        if(chat == null) {
            chat = new Chat();
        }
        return chat;
    }

    protected void setUserToUsersList(User user, PrintWriter writer) {
        usersList.put(user, writer);
    }

    protected Map<User, PrintWriter> getUsersList() {
        return usersList;
    }

}
