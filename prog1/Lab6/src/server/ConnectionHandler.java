package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {
    private ServerSocket serverSocket;

    public ConnectionHandler(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен на порту " + port);
    }

    public Socket acceptClient() throws IOException {
        return serverSocket.accept(); // Принимаем подключение
    }
}