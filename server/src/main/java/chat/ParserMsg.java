package chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

public class ParserMsg {
    // инициализирем логгер (log4j)
    private final static Logger logger = Logger.getLogger(ParserMsg.class);
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    /**
     * Парсит полученное сообщение в объект.
     * @param line Входящее сообщение.
     * @return Объект сообщения.
     */
    protected Message inputMsg(String line) {
        Message message;
        logger.debug("Parse msg: " + line + " to Message object.");
        message = gson.fromJson(line, Message.class);
        return message;
    }
    /**
     * Преобразует объект в JSON
     * @param message объект сообщение
     * @return JSON String
     */
    protected String outputMsg(Message message) {
        return gson.toJson(message);
    }
}
