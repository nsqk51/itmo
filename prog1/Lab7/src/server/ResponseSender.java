package server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ResponseSender {
    private final ObjectOutputStream out;

    public ResponseSender(ObjectOutputStream out) {
        this.out = out;
    }

    public void sendResponse(Object response) throws IOException {
        out.writeObject(response);
        out.flush();
    }
}