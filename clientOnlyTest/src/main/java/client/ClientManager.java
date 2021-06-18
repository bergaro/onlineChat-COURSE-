package client;

import org.apache.log4j.Logger;

import java.io.PrintWriter;

public class ClientManager {
    private final static Logger logger = Logger.getLogger(ClientManager.class);
    private final static ParserMsg parser = new ParserMsg();
    private final static String END_SESSION = "exit";
    private static ClientManager manager;
    private Message message;
    private String userName;

    private ClientManager() {}

    public static ClientManager getInstance() {
        if(manager == null) {
            manager = new ClientManager();
        }
        return manager;
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
//        out.println(request);
//        out.flush();
//        logger.info("Request send: " + request);
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
//        out.println(request);
//        out.flush();
//        logger.info("Request send: " + request);
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
//        out.println(msgJSON);
//        out.flush();
//        logger.info("Message send: " + msgJSON);
    }

    protected void sendMsg(PrintWriter out, String request) {
        out.println(request);
        out.flush();
        logger.info("Message send: " + request);
    }

    protected String getRegistrationMsg(String userName) {
        this.userName = userName;
        message = Message.buildMsg().setSenderName(userName).setMsgType(Message.NEW_USER);
        return parser.outputMsg(message);
    }

    protected String getDisconnectMsg(String userName) {
        message = Message.buildMsg().setSenderName(userName).setMsgType(Message.DISCONNECT);
        return parser.outputMsg(message);
    }

    protected boolean isRegister(String srvResponse) {
        Message response = parser.inputMsg(srvResponse);
        boolean state = false;
        if(response.getMsgStatus().equals(Message.TRUE_STATE)) {
            state = true;
            this.userName = response.getUserName();
        }
        return state;
    }

    protected String getSendMessageStr(String userTextLine) {
        message = Message.buildMsg()
                .setSenderName(userName)
                .setMsgType(Message.SEND_MSG)
                .setMessage(userTextLine);
        return parser.outputMsg(message);
    }

}
