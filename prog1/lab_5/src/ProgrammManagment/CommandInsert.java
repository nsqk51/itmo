package ProgrammManagment;

import Sup.InputReader;
import entity.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс команды 'insert'.
 * Добавляет транспортное средство в коллекцию.
 */
public class CommandInsert extends Command {

    /**
     * Выполняет команду 'insert'.
     * Запрашивает у пользователя данные для добавления нового транспортного средства в коллекцию.
     * Если ключ уже существует в коллекции, выводится ошибка.
     *
     * @param args аргументы команды (должен быть предоставлен 1 аргумент - ключ элемента).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("insert");

        // Проверка количества аргументов
        if (args.length != 1) {
            System.out.println("Ошибка: Данная команда должна содержать 1 аргумент (ключ элемента).");
            return;
        }

        try {
            Long key = Long.parseLong(args[0]);

            // Проверка, существует ли уже элемент с таким ключом в коллекции
            if (VehicleCollection.getVehicle().containsKey(key)) {
                System.out.println("Ошибка: Элемент с таким ключом уже существует.");
                return;
            }

            // Блок для ввода данных транспортного средства
            try {
                while (true) {

                    InputReader inputReader = new InputReader();

                    // Ввод имени транспорта
                    String name = null;
                    while (name == null || name.trim().isEmpty()) {
                        System.out.print("Введите название транспорта (не может быть пустым): ");
                        name = inputReader.readString().trim();
                        if (name.isEmpty()) {
                            System.out.println("Ошибка: Название транспорта не может быть пустым!");
                        }
                    }

                    // Ввод координаты X
                    Long x = null;
                    while (x == null || x <= -978) {
                        System.out.print("Введите координату X (целое число, минимум -978): ");
                        String xField = inputReader.readString();
                        try {
                            x = Long.parseLong(xField);
                            if (x <= -978) {
                                System.out.println("Ошибка: X должен быть больше -978!");
                                x = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: X должен быть числом.");
                        }
                    }

                    // Ввод координаты Y
                    Long y = null;
                    while (y == null) {
                        System.out.print("Введите координату Y (целое число, минимум -45) или оставьте пустым: ");
                        String yField = inputReader.readString();
                        if (yField.isEmpty()) {
                            break;
                        }
                        try {
                            y = Long.parseLong(yField);
                            if (y <= -45) {
                                System.out.println("Ошибка: Y должен быть больше -45!");
                                y = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: Y должен быть целым числом.");
                        }
                    }
                    // Ввод силы двигателя
                    Long enginePower = null;
                    while (enginePower == null) {
                        System.out.print("Введите силу двигателя (целое число, минимум 1) или оставьте пустым: ");
                        String enginePowerField = inputReader.readString();
                        if (enginePowerField.isEmpty()) {
                            break;
                        }
                        try {
                            enginePower = Long.parseLong(enginePowerField);
                            if (enginePower <= 0) {
                                System.out.println("Ошибка: сила двигателя должна быть больше 0!");
                                enginePower = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: сила двигателя должна быть целым числом.");
                        }
                    }

                    // Ввод потребления топлива
                    Float fuelConsumption = null;
                    while (fuelConsumption == null || fuelConsumption <= 0) {
                        System.out.print("Введите потребление топлива (целое число, минимум 1): ");
                        String fuelConsumptionField = inputReader.readString();
                        try {
                            fuelConsumption = Float.parseFloat(fuelConsumptionField);
                            if (fuelConsumption <= 0) {
                                System.out.println("Ошибка: потребление топлива должно быть больше 0!");
                                fuelConsumption = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: потребление топлива должно быть числом.");
                        }
                    }

                    // Ввод пройденной дистанции
                    Long distanceTravelled = null;
                    while (distanceTravelled == null || distanceTravelled <= 0) {
                        System.out.print("Введите пройденную дистанцию (целое число, минимум 1): ");
                        String distanceTravelledField = inputReader.readString();
                        try {
                            distanceTravelled = Long.parseLong(distanceTravelledField);
                            if (distanceTravelled <= 0) {
                                System.out.println("Ошибка: пройденная дистанция должна быть больше 0!");
                                distanceTravelled = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: пройденная дистанция должна быть числом.");
                        }
                    }

                    // Ввод типа транспорта
                    VehicleType type = null;
                    while (true) {
                        System.out.print("Введите тип транспорта (HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE) или оставьте пустым: ");
                        String typeField = inputReader.readString().trim();
                        if (typeField.isEmpty()) {
                            break;
                        }
                        try {
                            type = VehicleType.valueOf(typeField.toUpperCase());
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: Неверный тип транспорта. Допустимые значения: HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE.");
                        }
                    }

                    // Создание нового транспортного средства и добавление в коллекцию
                    Coordinates coordinates = new Coordinates(x, y);
                    Vehicle vehicle = new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);
                    VehicleCollection.getVehicle().put(key, vehicle);
                    System.out.println("Транспорт успешно добавлен в коллекцию!");
                    break;
                }
            } catch (NullPointerException e) {
                System.out.println();
                System.out.println("Ввод остановлен.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть целым числом.");
        }
    }
}