package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
	private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public ObjectOutputStream getOut() { 
    	return out; 
    }
    public ObjectInputStream getIn() { 
    	return in; 
    }

    public void close() throws IOException {
        socket.close();
    }
}