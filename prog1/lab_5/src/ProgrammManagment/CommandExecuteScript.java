package ProgrammManagment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import entity.Coordinates;
import entity.Vehicle;
import entity.VehicleCollection;
import entity.VehicleType;

/**
 * Команда для выполнения команд из файла (скрипта).
 * <p>
 * Позволяет автоматизировать выполнение команд, считывая их из указанного файла.
 * Поддерживает обработку вложенных скриптов и предотвращает зацикливание.
 * </p>
 */
public class CommandExecuteScript extends Command {
    private final CommandManager commandManager;
    private static final Set<String> executingScripts = new HashSet<>();

    /**
     * Конструктор команды.
     *
     * @param commandManager менеджер команд, используемый для выполнения команд из скрипта.
     */
    public CommandExecuteScript(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команды, считанные из указанного файла.
     * <p>
     * Проверяет существование файла, предотвращает зацикливание при вложенном вызове,
     * выполняет команды, включая добавление и обновление транспортных средств.
     * </p>
     *
     * @param args массив аргументов, содержащий путь к файлу скрипта.
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("execute_script");

        if (args.length != 1 || args[0].trim().isEmpty()) {
            System.out.println("Ошибка: Команда execute_script требует 1 аргумент (путь к файлу).");
            return;
        }

        File scriptFile = new File(args[0]).getAbsoluteFile();
        if (!scriptFile.exists() || !scriptFile.isFile()) {
            System.out.println("Ошибка: Файл не найден или это не файл.");
            return;
        }

        if (executingScripts.contains(scriptFile.getAbsolutePath())) {
            System.out.println("Ошибка: Обнаружено зацикливание! Файл '" + scriptFile.getAbsolutePath() + "' уже выполняется.");
            return;
        }

        executingScripts.add(scriptFile.getAbsolutePath());

        try (Scanner scanner = new Scanner(scriptFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] commandParts = line.split(" ", 2);
                String commandName = commandParts[0];
                String[] commandArgs = commandParts.length > 1 ? new String[]{commandParts[1].trim()} : new String[0];

                System.out.println(">> Выполняем команду: " + commandName);

                // Обработка команд insert и update
                if (commandName.equals("insert") || commandName.equals("update")) {
                    if (commandArgs.length != 1) {
                        System.out.println("Ошибка: Команда '" + commandName + "' должна содержать 1 аргумент (ключ элемента).");
                        continue;
                    }

                    Long key;
                    try {
                        key = Long.parseLong(commandArgs[0]);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Ключ должен быть числом.");
                        continue;
                    }

                    if (commandName.equals("insert") && VehicleCollection.getVehicle().containsKey(key)) {
                        System.out.println("Ошибка: Элемент с таким ключом уже существует!");
                        continue;
                    }
                    if (commandName.equals("update") && !VehicleCollection.getVehicle().containsKey(key)) {
                        System.out.println("Ошибка: Элемент с таким ключом не найден!");
                        continue;
                    }
                    String[] data = new String[7];  // 7 элементов данных
                    for (int i = 0; i < 7; i++) {
                        if (!scanner.hasNextLine()) {
                            System.out.println("Ошибка: Недостаточно данных для команды '" + commandName + "'.");
                            break;
                        }
                        data[i] = scanner.nextLine().trim();
                    }

                    Vehicle vehicle = parseVehicle(data);
                    if (vehicle == null) {
                        System.out.println("Ошибка: Неверный формат данных. Транспорт не добавлен.");
                        continue;
                    }

                    VehicleCollection.getVehicle().put(key, vehicle);
                    System.out.println("Транспорт успешно " + (commandName.equals("insert") ? "добавлен" : "обновлен") + " в коллекции!");
                } else if (commandName.equals("execute_script")) {
                    execute(commandArgs);  // рекурсивный вызов для вложенного скрипта
                } else {
                    if (!commandManager.executeCommand(commandName, commandArgs)) {
                        System.out.println("Ошибка: Команда не найдена.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл не найден.");
        } finally {
            executingScripts.remove(scriptFile.getAbsolutePath());
        }
    }

    /**
     * Разбирает данные из скрипта и создает объект транспортного средства.
     *
     * @param data массив строк с данными о транспортном средстве.
     * @return объект {@link Vehicle} или {@code null}, если данные некорректны.
     */
    private Vehicle parseVehicle(String[] data) {
        try {
            if (data[0] == null || data[0].isEmpty()) return null;
            String name = data[0];

            // Обработка координат
            Long x = Long.parseLong(data[1]);
            if (x <= -978) return null;

            Long y = Long.parseLong(data[2]);
            if (y <= -45) return null;
            Coordinates coordinates = new Coordinates(x, y);

            // Обработка характеристик транспорта
            Long enginePower = data[3].isEmpty() ? null : Long.parseLong(data[3]);
            if (enginePower != null && enginePower <= 0) return null;

            float fuelConsumption = Float.parseFloat(data[4]);
            if (fuelConsumption <= 0) return null;

            long distanceTravelled = Long.parseLong(data[5]);
            if (distanceTravelled <= 0) return null;

            // Обработка типа транспорта
            VehicleType type = null;
            if (data.length > 6 && !data[6].isEmpty()) {
                try {
                    type = VehicleType.valueOf(data[6].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: Неверный тип транспорта. Допустимые значения: HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE.");
                    return null;
                }
            }

            return new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);
        } catch (Exception e) {
            System.out.println("Ошибка при обработке данных транспортного средства: " + e.getMessage());
            return null;
        }
    }
}