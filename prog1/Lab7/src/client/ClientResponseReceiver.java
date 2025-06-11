
package client;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientResponseReceiver {
	private final ObjectInputStream in;

    public ClientResponseReceiver(ObjectInputStream in) {
        this.in = in;
    }

    public void getResponce() throws ClassNotFoundException, IOException {
    	String responce = (String) in.readObject();
    	System.out.println(responce);
    }
    
    public String getResponceString() throws ClassNotFoundException, IOException {
    	String responce = (String) in.readObject();
    	return responce;
    }
    
    public Object getData() throws ClassNotFoundException, IOException {
		Object data = (Object) in.readObject();
    	return data;
    }
}
