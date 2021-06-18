package chat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.net.Socket;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class chatTests {

    @Test
    public void chatManagerRouteNewUserTest() {
        Message request = Message.buildMsg()
                .setSenderName("Oleg")
                .setMsgType(ServerManager.NEW_USER);
        ServerManager serverManager = Mockito.spy(ServerManager.class);
        doNothing().when(serverManager).addNewUser(anyString(), any(Socket.class));
        serverManager.chatManager(request, new Socket());
        verify(serverManager, times(1)).addNewUser(anyString(), any(Socket.class));
    }
    @Test
    public void chatManagerRouteSendMsgTest() {
        Message request = Message.buildMsg()
                .setSenderName("Oleg")
                .setMsgType(ServerManager.SEND_MSG);
        ServerManager serverManager = Mockito.spy(ServerManager.class);
        doNothing().when(serverManager).addNewUser(anyString(), any(Socket.class));
        serverManager.chatManager(request, new Socket());
        verify(serverManager, times(1)).sendMessage(anyString(), any(Message.class));
    }
    @Test
    public void chatManagerRouteDisconnectTest() {
        Message request = Message.buildMsg()
                .setSenderName("Oleg")
                .setMsgType(ServerManager.DISCONNECT);
        ServerManager serverManager = Mockito.spy(ServerManager.class);
        doNothing().when(serverManager).addNewUser(anyString(), any(Socket.class));
        serverManager.chatManager(request, new Socket());
        verify(serverManager, times(1)).disconnected(anyString());
    }
    @Test
    public void parseMsgObjTest() {
        String expected = "{\"senderName\":\"Oleg\",\"msgType\":\"simpleMsg\",\"message\":\"Sth message\"}";
        Message request = Message.buildMsg()
                .setSenderName("Oleg")
                .setMessage("Sth message")
                .setMsgType(ServerManager.SEND_MSG);
        ParserMsg parserMsg = new ParserMsg();
        String actual = parserMsg.outputMsg(request);
        assertEquals(expected, actual);
    }

    @Test
    public void parseMsgJSONTest() {
        String request = "{\"senderName\":\"Oleg\",\"msgType\":\"simpleMsg\",\"message\":\"Sth message\"}";
        Message expected = Message.buildMsg()
                .setSenderName("Oleg")
                .setMessage("Sth message")
                .setMsgType(ServerManager.SEND_MSG);
        ParserMsg parserMsg = new ParserMsg();
        Message actual = parserMsg.inputMsg(request);
        assertEquals(expected, actual);
    }

}
