package server.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import server.user.PasswordUtils;

public class DatabaseUserManager {
    private Connection connection;
    
    public Connection getConnection() {
    	return connection;
    }

    public DatabaseUserManager(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Соединение с базой данных установлено.");
            }
            createTableIfNotExists();
            System.out.println("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                   + "username TEXT PRIMARY KEY,"
                   + "password_hash TEXT NOT NULL"
                   + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public boolean registerUser(String username, String rawPassword) throws SQLException {
        String hash = PasswordUtils.hashPassword(rawPassword);
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean authenticate(String username, String rawPassword) throws SQLException {
        String query = "SELECT password_hash FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return PasswordUtils.hashPassword(rawPassword).equals(storedHash);
            }
        }
        return false;
    }
    
    
    public ConcurrentHashMap<String, String> loadAllUsers() throws SQLException{
    	ConcurrentHashMap<String, String> usersFromBD = new ConcurrentHashMap<String, String>();
		String query = 
				"Select username AS name, password_hash AS password"
				+ " FROM users";
    	try(Statement stmt = connection.createStatement()){
    		ResultSet rs = stmt.executeQuery(query);
    		while(rs.next()) {
    			usersFromBD.put(rs.getString("name"), rs.getString("password"));
    		}
    	}
    	
    	return usersFromBD;
    	
    }
    
}