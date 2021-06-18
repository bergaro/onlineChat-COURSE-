package client;

import org.apache.log4j.Logger;

import java.io.PrintWriter;

public class ClientManager {
    private final static Logger logger = Logger.getLogger(ClientManager.class);
    private final static ParserMsg parser = new ParserMsg();
    private final static String END_SESSION = "exit";
    private Message message;
    private String userName;


    protected String getUserName() {
        return userName;
    }
    public String getEndSession() {
        return END_SESSION;
    }
    /**
     * Отправка запроса на подключение пользователя к серверу приложения
     * @param userTextLine имя пользователя
     * @param out поток отправки запросов на сервер
     * @return идентификатор метода(доп. проверки не включены)
     */
    public boolean connectUser(String userTextLine, PrintWriter out) {
        this.userName = userTextLine;
        message = Message.buildMsg().setSenderName(userName).setMsgType(Message.NEW_USER);
        String request = parser.outputMsg(message);
        sendMsg(out, request);
        return true;
    }
    /**
     * Отправка запроса на отключение пользователя от сервера приложения
     * @param out поток отправки запросов на сервер
     */
    public void disconnectUser(PrintWriter out) {
        message = Message.buildMsg().setSenderName(userName).setMsgType(Message.DISCONNECT);
        String request = parser.outputMsg(message);
        sendMsg(out, request);
    }
    /**
     * Отправка обычного сообщения на сервер приложения
     * @param message текстовое сообщение
     * @param out поток отправки запросов на сервер
     */
    public void sendMessage(String message, PrintWriter out) {
        Message request = Message.buildMsg()
                .setSenderName(userName)
                .setMsgType(Message.SEND_MSG)
                .setMessage(message);
        String msgJSON = parser.outputMsg(request);
        sendMsg(out, msgJSON);
    }

    protected void sendMsg(PrintWriter out, String request) {
        out.println(request);
        out.flush();
        logger.info("Message send: " + request);
    }

}
