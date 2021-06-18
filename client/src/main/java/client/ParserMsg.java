package client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

public class ParserMsg {
    private final static Logger logger = Logger.getLogger(ParserMsg.class);
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    /**
     * Парсит полученное сообщение в объект.
     * @param line Входящее сообщение.
     * @return Объект сообщения.
     */
    public Message inputMsg(String line) {
        Message message;
        logger.debug("Parse msg: " + line + " to Message object.");
        message = gson.fromJson(line, Message.class);
        return message;
    }
    /**
     * Преобразует объект Message в JSON
     * @param message объект сообщение
     * @return JSON String
     */
    public String outputMsg(Message message) {
        logger.debug("Parse objMsg: " + message + " to JSON object.");
        return gson.toJson(message);
    }
    /**
     * Преобразует JSON в объект Message
     * @param srvResponse
     * @return
     */
    public String viewMsg(String srvResponse) {
        Message response = inputMsg(srvResponse);
        return "Author: " + response.getUserName() + " | [time send : " + response.getDateTimeMsg() +"]"+
                "\nMessage: " + response.getMessage();
    }
}
