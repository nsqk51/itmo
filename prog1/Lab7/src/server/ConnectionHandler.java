package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionHandler {
    private ServerSocketChannel serverChannel;

    public ConnectionHandler(int port) throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        System.out.println("Сервер запущен на порту " + port);
    }

    public SocketChannel acceptClient() throws IOException {
        return serverChannel.accept(); 
        
    }
    
    
}