package chat;

import java.util.Objects;

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

    protected final static String FALSE_STATE = "ERROR";
    protected final static String TRUE_STATE = "NO error";

    protected Message() {

    }

    protected Message(String senderName, String msgType) {
        this.senderName = senderName;
        this.msgType = msgType;
    }


    protected Message(String senderName, String msgType, String message) {
        this.senderName = senderName;
        this.msgType = msgType;
        this.message = message;
    }

    protected Message(String senderName, String msgType, String message, String msgStatus) {
        this.senderName = senderName;
        this.msgType = msgType;
        this.message = message;
        this.msgStatus = msgStatus;
    }

    protected Message(String senderName, String msgType, String message, String msgStatus, String dateTimeMsg) {
        this.senderName = senderName;
        this.msgType = msgType;
        this.message = message;
        this.msgStatus = msgStatus;
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

    protected Message setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
        return this;
    }

    protected Message setMessage(String message) {
        this.message = message;
        return this;
    }

    protected Message setMsgDateTime(String dateTimeMsg) {
        this.dateTimeMsg = dateTimeMsg;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(senderName, message1.senderName) && Objects.equals(msgType, message1.msgType) && Objects.equals(message, message1.message) && Objects.equals(msgStatus, message1.msgStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderName);
    }

    @Override
    public String toString() {
        return "{" +
                "senderName: \"" + senderName + "\"" +
                ", msgType: \"" + msgType + "\"" +
                ", message: \"" + message + "\"" +
                ", msgStatus: \"" + msgStatus + "\"" +
                ", dateTimeMsg: \"" + dateTimeMsg + "\"" +
                '}';
    }
}
