package ProgrammManagment;

import java.util.Arrays;

import Sup.InputReader;
import Sup.Parse_to_Json;
import entity.VehicleCollection;

/**
 * Основной класс программы, который управляет запуском и выполнением команд.
 * <p>
 * Этот класс инициализирует компоненты, загружает данные о транспортных средствах из файла,
 * а затем запускает цикл, в котором пользователь может вводить команды для управления коллекцией.
 * Каждая команда выполняется через объект {@link CommandManager}, который передает команду в соответствующий обработчик.
 * </p>
 */
public class Main {
    
    /**
     * Главный метод программы.
     * <p>
     * Этот метод загружает транспортные средства из файла, указанного в переменной окружения {@code FILE_WITH_VEHICLE},
     * и запускает цикл, в котором пользователь может вводить команды.
     * Каждая введенная команда передается на выполнение через {@link CommandManager}.
     * </p>
     *
     * @param args Аргументы командной строки, которые не используются в данной версии программы.
     */
    public static void main(String[] args) {
        String fileName = System.getenv("FILE_WITH_VEHICLE");

        if (fileName == null || fileName.isEmpty()) {
            System.err.println("Ошибка: Переменная окружения FILE_WITH_VEHICLE не установлена.");
            return;
        }
        
        InputReader inputReader = new InputReader();
        CommandParser cmdPars = new CommandParser();
        CommandManager commandManager = new CommandManager();
        
        VehicleCollection.loadVehiclesFromFile(fileName);
        
        Parse_to_Json.writeVehiclesToFile(fileName);
        
        System.out.println("Введите команду help для ознакомления с доступными командами.");

        while (true) {
          try {
              System.out.print("> ");
              String str = inputReader.readString();
  
              String[] parts = cmdPars.parseCommandName(str);
              String commandName = parts[0];
              String[] argsArray = (parts.length > 1) ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
  
              commandManager.executeCommand(commandName, argsArray);
          } catch (java.lang.NullPointerException e) {
              System.exit(0);
              System.out.println("Программа остановлена.");
          }
        }
    }
}