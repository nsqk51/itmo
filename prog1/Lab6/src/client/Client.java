package client;

import java.io.*;
import java.net.Socket;
import java.util.*;

import client.ExecuteScript.ExecuteScript;
import client.SupRead.DataInput;
import client.SupCheck.CommandParser;
import client.SupCheck.DataCheck;
import client.SupCheck.InputCheck;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 2124;

    static DataInput dataInput = new DataInput();
    static InputCheck checkInput = new InputCheck();
    static Set<Long> existingKeys = new HashSet<>();
    static Set<Long> existingIds = new HashSet<>();
    public static Map<String, Boolean> additionalInput = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
  public static void main(String[] args) {

        String fileName = System.getenv("FILE_WITH_VEHICLE");

        if (fileName == null || fileName.isEmpty()) {
            System.err.println("Ошибка: Переменная окружения FILE_WITH_VEHICLE не установлена.");
            return;
        }

        CommandParser commandParser = new CommandParser();

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

          boolean exitRequested = false;
            // Добавление shutdown hook для сохранения коллекции при завершении программы
          Runtime.getRuntime().addShutdownHook(new Thread(() -> {
              if (exitRequested) {
                  try {
                      System.out.println("\nСохраняем коллекцию и завершаем работу...");
                      out.writeObject(new Object[]{"exit", new String[]{}});
                      out.flush();
                  } catch (IOException e) {
                      System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
                  }
              } else {
                  System.out.println("\nЗавершение без сохранения (exit не запрашивался).");
              }
          }));


            Object firstResponse = in.readObject();
            if (firstResponse instanceof String) {
                System.out.println((String) firstResponse);
                return;
            }
            additionalInput = (Map<String, Boolean>) firstResponse;

            // 2. Читаем ID и ключи
            Object secondResponse = in.readObject();
            if (secondResponse instanceof Set) {
                existingIds = (Set<Long>) secondResponse;
            }

            Object thirdResponse = in.readObject();
            if (thirdResponse instanceof Set) {
                existingKeys = (Set<Long>) thirdResponse;
            }


            System.out.println("Введите команду help для ознакомления с доступными командами.");

            // 3. Работаем в цикле, отправляем команды
            while (true) {
                System.out.print("> ");
                String input = null;
                
                try {
                    input = dataInput.input();
                } catch (Exception e) {
                    System.out.println("Ввод был прерван. Завершаем работу.");
                    break;  // Выход из цикла
                }

                if (input == null || input.trim().isEmpty()) {
                    System.out.println("\nВвод завершён пользователем. Завершаем работу клиента...");

                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
                    }

                    break;
                }


                if (input.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Завершение работы клиента...");
                    // Отправляем команду "exit" на сервер
                    out.writeObject(new Object[]{"exit", new String[]{}});                    // Получаем ответ от сервера
                    Object response = in.readObject();
                    if ("Клиент завершает работу. Коллекция фильмов сохранена.".equals(response)) {
                        System.out.println(response);
                        out.close();
                        in.close();
                        socket.close();
                        System.exit(0);  // Завершаем работу клиента
                    } else {
                        System.out.println("Ошибка: не удалось сохранить коллекцию.");
                    }
                }

                // Обработка других команд
                String[] commandParts = commandParser.parseCommandName(input);
                String commandName = commandParts[0];

                if (commandName.equals("execute_script")) {
                    String scriptFileName = commandParts.length > 1 ? commandParts[1] : null;
                    if (scriptFileName == null) {
                        System.out.println("Ошибка: команда execute_script требует имя файла.");
                        continue;
                    }

                    ExecuteScript executeScriptHandler = new ExecuteScript();
                    executeScriptHandler.executeScript(scriptFileName, out, in, additionalInput);
                    continue;
                }

                String[] argsToSend;
                boolean needsAdditionalInput = additionalInput.getOrDefault(commandName, false);
                boolean hasArgument = commandParts.length > 1;

                if (needsAdditionalInput && hasArgument) {
                    Long key = 0L;
                    if (DataCheck.isInteger(commandParts[1])) {
                        key = Long.parseLong(commandParts[1]);
                    } else {
                        System.out.println("Ошибка: параметр должен быть числом.");
                        continue;
                    }
                    
                    if (commandName.equals("insert") && existingKeys.contains(key)) {
                        System.out.println("Ошибка: ключ " + key + " уже существует.");
                        continue;
                    }

                    if (commandName.equals("update") && !existingIds.contains(key)) {
                        System.out.println("Ошибка: id " + key + " не найден.");
                        continue;
                    }


                    String[] movieData = checkInput.readVehicleInput();
                    if (movieData == null) {
                        System.out.println("Ввод остановлен.");
                        continue;
                    }
                    String[] argId = new String[]{String.valueOf(key)};
                    argsToSend = new String[argId.length + movieData.length];
                    System.arraycopy(argId, 0, argsToSend, 0, argId.length);
                    System.arraycopy(movieData, 0, argsToSend, argId.length, movieData.length);
                } else if (needsAdditionalInput) {
                    String[] movieData = checkInput.readVehicleInput();
                    if (movieData == null) {
                        System.out.println("Ввод остановлен.");
                        continue;
                    }
                    argsToSend = movieData;
                } else if (hasArgument) {
                    argsToSend = new String[]{String.valueOf(commandParts[1])};
                } else {
                    argsToSend = new String[]{};  // Пустые аргументы
                }

                // Отправляем команду и аргументы на сервер
                out.writeObject(new Object[]{commandName, argsToSend});
                out.flush();

                // Получаем ответ сервера
                Object response = in.readObject();
                if (response instanceof String) {
                    String serverResponse = (String) response;
                    System.out.println("Ответ от сервера: " + serverResponse);                    //  Добавим ключ, если insert прошёл успешно
                    if (commandName.equals("insert") && serverResponse.toLowerCase().contains("успешно")) {
                        try {
                            Long newKey = Long.parseLong(commandParts[1]);
                            existingKeys.add(newKey);
                            Long insertedKey = Long.parseLong(argsToSend[0]);
                            existingIds.add(insertedKey);
                        } catch (NumberFormatException ignored) {}
                    }
                } else {
                    System.out.println("Ошибка: сервер прислал некорректный ответ.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
