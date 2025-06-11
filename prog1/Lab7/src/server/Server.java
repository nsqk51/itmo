package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

import server.entity.VehicleCollection;
import server.entity.IdGeneration;
import server.database.DatabaseVehicleManager;
import server.database.DatabaseUserManager;
import server.user.UserManager;

import java.util.concurrent.*;


public class Server {
    private static final int PORT = 2121;
    
    // Пулы потоков
    private static final ExecutorService readPool = Executors.newFixedThreadPool(10); // Чтение
    private static final ExecutorService processPool = Executors.newCachedThreadPool(); // Обработка
    private static final ExecutorService responsePool = Executors.newCachedThreadPool(); // Ответы
    
    //static final UserManager userManager = new UserManager();
    static String dbUrl = "jdbc:postgresql://localhost:5433/studs";
    static String dbUser = "s464906";
    static String dbPassword = "eRr1w4OlGDX47W7t";

    // Объекты для взаимодействия с БД
    public static DatabaseUserManager userManager = new DatabaseUserManager(dbUrl, dbUser, dbPassword);
    public static DatabaseVehicleManager vehicleManager = new DatabaseVehicleManager(userManager.getConnection());
    public static IdGeneration idGeneration = new  IdGeneration(userManager.getConnection());
    
    public static UserManager allUsers;
    
    public static void main(String[] args) {
        try {
            
            try {
            	vehicleManager.initializeDatabase();
				VehicleCollection vehicles = new VehicleCollection(vehicleManager.loadAllVehicles());
				allUsers = new UserManager(userManager.loadAllUsers());
			} catch (SQLException e) {

				System.out.println("Ошибка при загрузке данных из БД: "+  e.getMessage());
			}
            ConnectionHandler connectionHandler = new ConnectionHandler(PORT);
            
            
            while (true) {
                SocketChannel clientChannel = connectionHandler.acceptClient();
                if (clientChannel != null) {
                    System.out.println("Подключился новый клиент.");
                    
                    readPool.execute(() -> {
                        try {
                            RequestHandler handler = new RequestHandler(clientChannel, processPool, responsePool);
                            handler.handleClient();
                        } catch (IOException | SQLException e) {
                            //logger.warning("Ошибка при создании RequestHandler: " + e.getMessage());
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

