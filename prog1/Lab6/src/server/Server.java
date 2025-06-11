package server;

import java.io.IOException;
import java.net.Socket;

public class Server {
    private static final int PORT = 2124;
    public static void main(String[] args) {
        try {
            
            ConnectionHandler connectionHandler = new ConnectionHandler(PORT);

            while (true) {
                Socket clientSocket = connectionHandler.acceptClient();
                if (clientSocket != null) {
                    System.out.println("Подключился новый клиент.");
                    RequestHandler requestHandler = new RequestHandler(clientSocket);
                    requestHandler.handleClient();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}