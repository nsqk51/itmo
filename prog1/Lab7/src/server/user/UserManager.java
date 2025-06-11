package server.user;

import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
	
    public ConcurrentHashMap<String, String> userStore = new ConcurrentHashMap<>();
    
    public UserManager(ConcurrentHashMap<String, String> users) {
    	this.userStore = users;
    }
    
    public boolean registerUser(String username, String rawPassword) {
        String hash = PasswordUtils.hashPassword(rawPassword);
        return (userStore.putIfAbsent(username, hash) == null);
    }

    public boolean authenticate(String username, String rawPassword) {
        String hash = PasswordUtils.hashPassword(rawPassword);
        return hash.equals(userStore.get(username));
    }
    
    
}
