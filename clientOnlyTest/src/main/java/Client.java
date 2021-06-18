import client.ClientManager;
import client.Message;
import client.ParserMsg;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    // инициализирем логгер (log4j)
    private final static Logger logger = Logger.getLogger(Client.class);
    // Свойства приложения
    private static Properties properties;
    private static final ClientManager manager = ClientManager.getInstance();

    public static void main(String[] args) {
        getAppProperties();
        int port = Integer.parseInt(properties.getProperty("connect.port"));
        String host = properties.getProperty("connect.host");
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            Scanner sc = new Scanner(System.in);
            logger.info("Start client...");
            System.out.println("Input user name:");
            String line = sc.nextLine();
            if(manager.connectUser(line, out)) {
                Thread writer = new Thread(getWriter(out, sc), "writer");
                Thread listener = new Thread(getListener(in), "listener");
                listener.start();
                logger.debug("listener thread started");
                writer.start();
                logger.debug("writer thread started");
                while (true) {
                    if(!listener.isAlive()) {
                        System.out.println("User disconnected.");
                        logger.info("Disconnected client...");
                        break;
                    }
                }
            }
            sc.close();
        }
        catch (IOException e) {
            logger.error("Error: " + e);
        }
    }
    /**
     * инициализирует properties
     */
    private static void getAppProperties() {
        try {
            properties = new Properties();
            FileReader reader = new FileReader("config.properties");
            properties.load(reader);
            reader.close();
        } catch (IOException e) {
            logger.error("Error: ", e);
        }
    }
    /**
     * Возвращает поток который выполняет функцию отправителя сообщений на сервер.
     * @param out объект "писатель"
     * @param sc Scanner
     * @return Runnable задача
     */
    private static Runnable getWriter(PrintWriter out, Scanner sc) {
        return () -> {
            while (true) {
                String line;
                System.out.println("Please inputMsg...");
                line = sc.nextLine();
                if(line.equals(manager.getEndSession())) {
                    manager.disconnectUser(out);
                    System.out.println("User disconnected.");
                    break;
                }
                manager.sendMessage(line, out);
            }
            Thread.currentThread().interrupt();
            logger.debug("writer thread interrupted");
        };
    }
    /**
     * Возвращает поток который выполняет функцию слушателя сообщений от сервера.
     * @param in входящий поток
     * @return Runnable задача
     */
    private static Runnable getListener(BufferedReader in) {
        return () -> {
            ParserMsg parserMsg = new ParserMsg();
            Message message;
            String response;
            try{
                while (true) {
                    response = in.readLine();
                    if(response != null) {
                        message = parserMsg.inputMsg(response);
                        if(message.getMsgType().equals(Message.SEND_MSG)) {
                            System.out.println(parserMsg.viewMsg(response));
                            logger.debug("Response: " + parserMsg.viewMsg(response));
                            System.out.println("Please inputMsg...");
                        } else if(message.getMsgType().equals(Message.DISCONNECT)) {
                            break;
                        }
                    }
                }
                Thread.currentThread().interrupt();
                logger.debug("listener thread interrupted");
            }catch (IOException ex) {
                ex.printStackTrace();
                logger.error("Error(listener): " + ex);
            }
        };
    }
}
