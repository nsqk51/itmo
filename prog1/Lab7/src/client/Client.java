package client;

import java.io.*;
import java.net.ConnectException;
import java.util.*;

import client.SupRead.DataInput;
import client.SupCheck.DataCheck;
import client.SupCheck.InputCheck;
import client.Data.CurrentClient;
import client.Data.DataForVehicle;
import client.SupCheck.CommandParser;
import client.ExecuteScript.ExecuteScript;
import client.ExecuteScript.FileStack;


public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 2121;
    
    static DataInput dataInput = new DataInput();
    static InputCheck checkInput = new InputCheck();
    static Set<Long> existingKeys = new HashSet<>();
    static Set<Long> existingIds = new HashSet<>();
    static Map<Long, String> idOwners = new HashMap<>();
    public static CurrentClient currentClient;
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {



        CommandParser commandParser = new CommandParser();
        ExecuteScript executeScript = new ExecuteScript();

        try {
        	 ClientConnection connection = new ClientConnection();
        	 connection.connect(SERVER_HOST, SERVER_PORT);
        	 ClientRequestSender sender = new ClientRequestSender(connection.getOut());
             ClientResponseReceiver receiver = new ClientResponseReceiver(connection.getIn());
             String input;
             
             while (true) {
            	 System.out.println("Зарегистрируйтесь или авторизуйтесь в системе введите \"join\" или \"register\" ");
            	 input = dataInput.input();
            	 if (input.equalsIgnoreCase("join") || input.equalsIgnoreCase("register")) {
            		    sender.send(input); 
            		    receiver.getResponce(); 

            		    System.out.print("Логин: ");
            		    String login = dataInput.input();

            		    System.out.print("Пароль: ");
            		    String password = dataInput.input();

            		    sender.send(new String[]{login, password}); 
            		    
            		    String responce = (String) receiver.getData();
            		    
            		    if(responce.equals("OK")) {
            		    	System.out.println("Welcome");
            		    	currentClient = new CurrentClient(login, password);
            		    	break;
            		    }
            		    else {
            		    	System.out.println(responce);
            		    }
            		    
            	 }
            	 else {
            		 System.out.println("Введена некорректная команда");
            	 }
            	 
             }
             
             System.out.println("Введите команду help для ознакомления с доступными командами.");
             
             new DataForVehicle(receiver);
             
             Object secondResponse = receiver.getData();
             if (secondResponse instanceof Set) {
                 existingIds = (Set<Long>) secondResponse;
             }

             Object thirdResponse = receiver.getData();
             if (thirdResponse instanceof Set) {
                 existingKeys = (Set<Long>) thirdResponse;
             }
             
             Object fourthResponse = receiver.getData();
             if (fourthResponse instanceof Map) {
                 idOwners = (Map<Long, String>) fourthResponse;
             }

            while (true) {
                System.out.println("> ");
                
                input = dataInput.input();

                if (input.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }

                if (commandParser.parseCommandName(input)[0].equals("execute_script")) {
                    String scriptFileName = commandParser.parseCommandName(input)[1];
                    executeScript.executeScript(scriptFileName, connection.getOut(), connection.getIn(), DataForVehicle.additionalInput);
                    FileStack.fileStack.clear();
                    continue;
                }

                String[] commandParts = commandParser.parseCommandName(input);
                String commandName = commandParts[0];

                String[] argsToSend;
                boolean needsAdditionalInput = DataForVehicle.additionalInput.getOrDefault(commandName, false);
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
                    
                    if (commandName.equals("replace_if_lowe") && !existingKeys.contains(key)) {
                        System.out.println("Ошибка: Ключ " + key + " не найден.");
                        continue;
                    }
                    
                    if (commandName.equalsIgnoreCase("replace_if_lowe")) {
                        if (!idOwners.containsKey(key) || !idOwners.get(key).equals(currentClient.getUserName())) {
                            System.out.println("Ошибка: у вас нет прав на изменение этого фильма.");
                            continue;
                        }
                    }
                    
                    if (commandName.equalsIgnoreCase("update")) {
                        if (!idOwners.containsKey(key) || !idOwners.get(key).equals(currentClient.getUserName())) {
                            System.out.println("Ошибка: у вас нет прав на изменение этого фильма.");
                            continue;
                        }
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
                    
                
                String login = currentClient.getUserName();
                String password = currentClient.getUserPassword();
                sender.send(new Object[]{commandName, argsToSend, login, password});
                receiver.getResponce();

             // Получение свежих данных о ключах, ID и владельцах
                Object secondResponse1 = receiver.getData();
                if (secondResponse1 instanceof Set) {
                    existingIds = (Set<Long>) secondResponse1;
                }

                Object thirdResponse1 = receiver.getData();
                if (thirdResponse1 instanceof Set) {
                    existingKeys = (Set<Long>) thirdResponse1;
                }

                Object fourthResponse1 = receiver.getData();
                if (fourthResponse1 instanceof Map) {
                    idOwners = (Map<Long, String>) fourthResponse1;
                }

            }
        } 
        catch (ConnectException e) {
            System.out.println("Сервер занят, подождите немного.");
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
