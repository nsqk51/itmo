package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientRequestSender {
	private final ObjectOutputStream out;

    public ClientRequestSender(ObjectOutputStream out) {
        this.out = out;
    }

    public void send(Object command) throws IOException {
        out.writeObject(command);
        out.flush();
    }
}