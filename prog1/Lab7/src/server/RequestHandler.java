package server;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import server.ProgrammManagment.ExecuteCommand;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.ProgrammManagment.CommandManager;

public class RequestHandler {
    private ObjectInputStream in;
    private ResponseSender responseSender;
    private final ExecutorService processPool;
    private final ExecutorService responsePool;
    Set<Long> usedIds = new HashSet<>();
    Set<Long> usedKeys = new HashSet<>();
    Map<Long, String> idOwners;
    private Map<String, Boolean> additionalInputForCommand;

    public RequestHandler(SocketChannel clientChannel, ExecutorService processPool, ExecutorService responsePool) throws IOException {
        this.processPool = processPool;
        this.responsePool = responsePool;

        ObjectOutputStream out = new ObjectOutputStream(clientChannel.socket().getOutputStream());
        in = new ObjectInputStream(clientChannel.socket().getInputStream());
        responseSender = new ResponseSender(out);
    }

    public void handleClient() throws SQLException {
        try {
            // Цикл аутентификации / регистрации
            while (true) {
                String entrance = (String) in.readObject();
                if (entrance.trim().equalsIgnoreCase("join")) {
                    responseSender.sendResponse("Введите логин и пароль");
                    String[] credentials = (String[]) in.readObject();
                    String login = credentials[0];
                    String password = credentials[1];

                    if (Server.allUsers.authenticate(login, password)) {
                        responseSender.sendResponse("OK");
                        break;
                    } else {
                        responseSender.sendResponse("Неверный логин или пароль");
                    }
                } else if (entrance.trim().equalsIgnoreCase("register")) {
                    responseSender.sendResponse("Введите логин и пароль");
                    String[] credentials = (String[]) in.readObject();
                    String login = credentials[0];
                    String password = credentials[1];

                    if (Server.allUsers.registerUser(login, password)) {
                        Server.userManager.registerUser(login, password);
                        responseSender.sendResponse("OK");
                        break;
                    } else {
                        responseSender.sendResponse("Пользователь уже существует");
                    }
                } else {
                    responseSender.sendResponse("Введена неверная команда");
                }
            }

            // Отправка данных клиенту после успешной аутентификации/регистрации

            // 1. Информация о том, какие команды требуют дополнительного ввода
            additionalInputForCommand = new CommandManager().getCommandsWithInputRequirement();
            responseSender.sendResponse(additionalInputForCommand);

            for (Map.Entry<Long, Vehicle> entry : VehicleCollection.vehicles.entrySet()) {
                usedKeys.add(entry.getKey());
                usedIds.add(Long.valueOf(entry.getValue().getId()));
            }
            
            idOwners = Server.vehicleManager.getIdOwners();

            // Отправляем клиенту в порядке, который ожидает клиент:
            // idOwners, usedIds, usedKeys
            responseSender.sendResponse(usedIds);
            responseSender.sendResponse(usedKeys);
            responseSender.sendResponse(idOwners);

            // Далее - основной цикл обработки команд от клиента
            while (true) {
                Object received = in.readObject();
                if (received instanceof Object[]) {
                    Object[] receivedArray = (Object[]) received;

                    if (receivedArray.length != 4 || !(receivedArray[0] instanceof String) || !(receivedArray[1] instanceof Object[])) {
                        responseSender.sendResponse("Ошибка: Некорректный формат команды.");
                        continue;
                    }

                    String commandName = (String) receivedArray[0];
                    String[] commandArgs = (String[]) receivedArray[1];
                    String login = (String) receivedArray[2];
                    String password = (String) receivedArray[3];

                    System.out.println("Получена команда: " + commandName);

                    processPool.execute(() -> {
                        try {
                            String response = ExecuteCommand.executeCommand(commandName, commandArgs, login, password);

                            responsePool.execute(() -> {
                                try {
                                    responseSender.sendResponse(response);

                                    // Отправить обновлённые данные
                                    Set<Long> usedIds = new HashSet<>();
                                    Set<Long> usedKeys = new HashSet<>();
                                    for (Map.Entry<Long, Vehicle> entry : VehicleCollection.vehicles.entrySet()) {
                                        usedKeys.add(entry.getKey());
                                        usedIds.add(Long.valueOf(entry.getValue().getId()));
                                    }
                                    idOwners = Server.vehicleManager.getIdOwners();

                                    responseSender.sendResponse(usedIds);
                                    responseSender.sendResponse(usedKeys);
                                    responseSender.sendResponse(idOwners);

                                } catch (IOException e) {
                                    System.out.println("Ошибка отправки ответа: " + e.getMessage());
                                } catch (SQLException e) {
                                	System.out.println("Ошибка выполнения команды: SQL");
								}
                            });

                        } catch (Exception e) {
                            System.out.println("Ошибка выполнения команды: " + e.getMessage());
                        }
                    });

                } else {
                    System.out.println("Ошибка: получен некорректный объект.");
                }
            }
        } catch (EOFException e) {
            System.out.println("Клиент отключился.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }

}
