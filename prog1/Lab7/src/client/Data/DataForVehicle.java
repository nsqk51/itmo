package client.Data;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import client.ClientResponseReceiver;

public class DataForVehicle {
	
	public static Map<String, Boolean> additionalInput = new LinkedHashMap<>();
    
    @SuppressWarnings("unchecked")
	public DataForVehicle(ClientResponseReceiver receiver) throws ClassNotFoundException, IOException {
    	
		additionalInput = (Map<String, Boolean>) receiver.getData();
    }
}