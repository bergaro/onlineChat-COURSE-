import chat.Message;
import chat.ServerManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // инициализирем логгер (log4j)
    private final static Logger logger = Logger.getLogger(Server.class);
    // Свойства приложения
    private static Properties properties;

    public static void main(String[] args) {
        getAppProperties();
        int port = Integer.parseInt(properties.getProperty("connect.port"));
        try (ServerSocket server = new ServerSocket(port)) {
            server.setReuseAddress(true);
            logger.info("Server started...");
            // Пул потоков
            ExecutorService threadPool = Executors.newCachedThreadPool();
            while (true) {
                Socket client = server.accept();
                threadPool.execute(new ClientHandler(client));
            }
        } catch (IOException e) {
            logger.error("Error: ", e);
        }
    }
    /**
     * инициализирует обект Properties данными из файла свойств приложения: config.properties
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
     * Вложенный клас описывающий действие потоков
     */
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                ServerManager manager = new ServerManager();
                String line;
                Message response;
                while ((line = in.readLine()) != null) {
                    String client = Thread.currentThread().getName();
                    logger.info(client + " send from the message: " + line);
                    response = manager.processingRequest(clientSocket, line);
                    if (response.getMsgType().equals(manager.getDisconnectState())) {
                        logger.info(client + " is disconnected...");
                        break;
                    }
                    logger.info("Response: " + response);
                }
            } catch (IOException e) {
                logger.error("Error: ", e);
            }
        }
    }
}
