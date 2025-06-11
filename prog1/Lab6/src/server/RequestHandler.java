package server;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server.ProgrammManagment.CommandManager;
import server.ProgrammManagment.CommandSave;
import server.ProgrammManagment.ExecuteCommand;
import server.Sup.ParseFromJson;
import server.entity.Vehicle;
import server.entity.VehicleCollection;

public class RequestHandler {
    private ObjectInputStream in;
    private ResponseSender responseSender;
    private Map<String, Boolean> additionalInputForCommand;
    CommandSave save = new CommandSave();

    public RequestHandler(Socket clientSocket) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(clientSocket.getInputStream());
        responseSender = new ResponseSender(out);

        additionalInputForCommand = new CommandManager().getCommandsWithInputRequirement();
        responseSender.sendResponse(additionalInputForCommand);
    }

    public void handleClient() {
      
      boolean exitHandled = false;
      
        try {
            String fileName = System.getenv("FILE_WITH_VEHICLE");

            if (fileName == null || fileName.isEmpty()) {
                responseSender.sendResponse("Ошибка: переменная окружения FILE_WITH_VEHICLE не установлена.");
                return;
            }

            File file = new File("Files", fileName); // предполагаем, что все файлы в папке "Files"
            if (!file.exists() || !file.isFile()) {
                responseSender.sendResponse("Ошибка: файл " + file.getAbsolutePath() + " не найден.");
                return;
            }

            loadVehiclesFromJson(file.getAbsolutePath()); // загрузить коллекцию
            
            Set<Long> usedIds = new HashSet<>();
            Set<Long> usedKeys = new HashSet<>();

            for (Map.Entry<Long, Vehicle> entry : VehicleCollection.vehicles.entrySet()) {
                usedKeys.add(entry.getKey());         // ключ — это ключ из Map
                usedIds.add(Long.valueOf(entry.getValue().getId())); // id — из объекта Movie
            }

            responseSender.sendResponse(usedIds);
            responseSender.sendResponse(usedKeys);

            while (true) {
                Object received = in.readObject();
                if (received instanceof Object[]) {
                    Object[] receivedArray = (Object[]) received;

                    if (receivedArray.length != 2 || !(receivedArray[0] instanceof String) || !(receivedArray[1] instanceof Object[])) {
                        responseSender.sendResponse("Ошибка: некорректный формат команды.");
                        continue;
                    }

                    String commandName = (String) receivedArray[0];
                    String[] commandArgs = (String[]) receivedArray[1];

                    System.out.println("Получена команда: " + commandName);

                    // Выполняем команду
                    String response = ExecuteCommand.executeCommand(commandName, commandArgs);
                    // Отправляем результат
                    
                    if ("exit".equals(commandName)) {
                        exitHandled = true; // помечаем, что сохранение уже выполнено
                    }
                    
                    responseSender.sendResponse(response);
                } else {
                    System.out.println("Ошибка: получен некорректный объект.");
                }
            }
        } catch (EOFException e) {
            System.out.println("Клиент отключился.");
            if (!exitHandled) {
                save.execute(new String[]{}); // выполняем сохранение ТОЛЬКО если не было exit
            }
        } catch (IOException | ClassNotFoundException e) {
            save.execute(new String[]{});
            System.out.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }    private static void loadVehiclesFromJson(String filePath) {
        try {
            ParseFromJson parser = new ParseFromJson(filePath); // создаём объект парсера
              List<Vehicle> vehicles = parser.parseVehicles(); // парсим фильмы

              VehicleCollection.vehicles.clear(); // очищаем старую коллекцию

              for (Vehicle vehicle : vehicles) {
                  VehicleCollection.vehicles.put(Long.valueOf(vehicle.getId()), vehicle); // добавляем фильм по id
              }

              System.out.println("Коллекция фильмов загружена из файла: " + filePath);
          } catch (Exception e) {
              System.out.println("Ошибка загрузки коллекции: " + filePath);
              e.printStackTrace();
          }
      }

  }