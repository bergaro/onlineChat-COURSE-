package client;

public class Message {
    // имя автора сообщения
    private String senderName;
    // Тип сообщения
    private String msgType;
    // Текст сообщения
    private String message;
    // Статус сообщения
    private String msgStatus;
    // Время отправки
    private String dateTimeMsg;

    public final static String NEW_USER = "connected";
    public final static String SEND_MSG= "simpleMsg";
    public final static String DISCONNECT= "disconnect";

    protected final static String FALSE_STATE = "ERROR";
    protected final static String TRUE_STATE = "NO error";

    private Message() {

    }

    protected Message(String senderName, String msgType) {
        this.senderName = senderName;
        this.msgType = msgType;
    }

    protected Message(String senderName, String msgType, String message, String dateTimeMsg) {
        this.senderName = senderName;
        this.msgType = msgType;
        this.message = message;
        this.dateTimeMsg = dateTimeMsg;
    }

    protected String getUserName() {
        return senderName;
    }

    public String getMsgType() {
        return msgType;
    }

    protected String getMessage() {
        return message;
    }

    protected String getMsgStatus() {
        return msgStatus;
    }

    protected String getDateTimeMsg() {
        return dateTimeMsg;
    }

    protected static Message buildMsg() {
        return new Message();
    }

    protected Message setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    protected Message setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    protected Message setMessage(String message) {
        this.message = message;
        return this;
    }


}
