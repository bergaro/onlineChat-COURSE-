package chat;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ServerManager {
    // инициализирем логгер (log4j)
    private final static Logger logger = Logger.getLogger(ServerManager.class);
    // Метка для серверных сообщений
    private final static String SRV_MSG = "srvMsg";
    // Типы сообщений обрабатываемых сервером
    protected final static String NEW_USER = "connected";
    protected final static String SEND_MSG= "simpleMsg";
    protected final static String DISCONNECT= "disconnect";
    // Парсер входящего сообщения
    private final ParserMsg parser = new ParserMsg();
    // Объект - Чат
    private final Chat chat = Chat.getInstance();

    public String getDisconnectState() {
        return DISCONNECT;
    }
    /**
     * Парсит сообщение в объект - Message и передаёт его обраотчику чата.
     * @param socket подключение (клиент-сервер)
     * @return ответ сервера
     */
    public Message processingRequest(Socket socket, String msg) {
        Message message = parser.inputMsg(msg);
        return chatManager(message, socket);
    }
    /**
     * В зависиомтсти от типа запроса определяет его маршрут.
     * @param msg Вохдящее сообщение
     * @param socket Соединение (клиент-сервер)
     * @return Ответ сервера
     */
    protected Message chatManager(Message msg, Socket socket) {
        Message response = Message.buildMsg()
                                .setMessage(msg.getUserName())
                                .setMsgType(SRV_MSG)
                                .setMsgStatus(Message.TRUE_STATE)
                                .setMessage("in processing");
        String msgTypeRequest = msg.getMsgType();
        String senderName = msg.getUserName();
        String logMsg;
        switch (msgTypeRequest) {
            // Добавление нового пользователя в чат
            case NEW_USER -> {
                addNewUser(senderName, socket);
                logger.info("Route: " + NEW_USER);
            }
            // Отправка сообщения
            case SEND_MSG -> {
                sendMessage(senderName, msg);
                logger.info("Route: " + SEND_MSG);
            }
            // Отключение от чата
            case DISCONNECT -> {
                response = disconnected(senderName);
                logger.info("Route: " + DISCONNECT);
            }
        }
        logMsg = parser.outputMsg(response);
        logger.debug("Server answer: " + logMsg);
        return response;
    }
    /**
     * Добавляет нового пользователя в чат, сохраняя PrintWriter сокета для отправки.
     * @param senderName имя нового пользователя.
     * @param socket Сокет соединения.
     */
    protected void addNewUser(String senderName, Socket socket) {
        User user = new User(senderName);
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            chat.setUserToUsersList(user, writer);
            Message message = Message.buildMsg()
                    .setSenderName(senderName)
                    .setMsgType(NEW_USER)
                    .setMsgStatus(Message.TRUE_STATE)
                    .setMessage("User connected.");
            writer.println(parser.outputMsg(message));
            writer.flush();
        } catch (IOException ex) {
            logger.error("Error: " + ex);
        }
    }
    /**
     * Отправляет сообщение пользователя всем участникам чата, кроме самого пользователя.
     * Собирает ответ сервера.
     * @param senderName имя пользователя.
     * @param msg Сообщение для отправки.
     */
    protected void sendMessage(String senderName, Message msg) {
        PrintWriter writer;
        Map<User, PrintWriter> chatUsers = chat.getUsersList();
        Date dateSend = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String sentTime = simpleDateFormat.format(dateSend);
        Message response = Message.buildMsg()
                .setSenderName(senderName)
                .setMsgType(SEND_MSG)
                .setMsgStatus(Message.TRUE_STATE)
                .setMsgDateTime(sentTime)
                .setMessage(msg.getMessage());
        for(User user: chatUsers.keySet()) {
            if(!user.getUserName().equals(senderName)) {
                writer = chatUsers.get(user);
                writer.println(parser.outputMsg(response));
                writer.flush();
                logger.info("Message {" + msg + "} sent to user: " + user.getUserName());
            }
        }
    }
    /**
     * Удаляет пользователя и его соединение из чата.
     * Собирает ответ сервера.
     * @param senderName имя пользователя.
     * @return Ответ сервера.
     */
    protected Message disconnected(String senderName) {
        Message response = Message.buildMsg()
                .setSenderName(senderName)
                .setMsgType(DISCONNECT)
                .setMsgStatus(Message.TRUE_STATE)
                .setMessage("User disconnected from chat.");
        for(User user : chat.getUsersList().keySet()) {
            if(user.getUserName().equals(senderName)) {
                PrintWriter out = chat.getUsersList().get(user);
                out.println(parser.outputMsg(response));
                out.flush();
                out.close();
                chat.getUsersList().remove(user);
                logger.debug("User: " + user.getUserName() + " disconnected");
            }
        }
        return response;
    }
}
