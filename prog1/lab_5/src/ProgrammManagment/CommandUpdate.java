package ProgrammManagment;

import java.time.LocalDateTime;

import Sup.InputReader;
import entity.Coordinates;
import entity.Vehicle;
import entity.VehicleCollection;
import entity.VehicleType;

/**
 * Команда для обновления информации о транспортном средстве в коллекции.
 * <p>
 * Эта команда позволяет обновить данные транспортного средства в коллекции по заданному ключу.
 * Вводятся новые значения для полей транспортного средства, таких как имя, координаты, сила двигателя,
 * потребление топлива, дистанция и тип транспорта. Если транспорт с заданным ключом найден в коллекции,
 * он будет заменен новыми значениями.
 * </p>
 */
public class CommandUpdate extends Command {

    /**
     * Выполняет команду обновления транспортного средства по указанному ключу.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Проверяет, что в команде присутствует ровно один аргумент (ключ).</li>
     *   <li>Если транспорт с таким ключом найден в коллекции, то удаляется старое транспортное средство.</li>
     *   <li>Запрашивает у пользователя новые данные для обновления транспортного средства.</li>
     *   <li>Проверяет корректность введенных данных (например, координаты, сила двигателя и т. д.).</li>
     *   <li>Если все данные корректны, новое транспортное средство добавляется в коллекцию с тем же ключом.</li>
     *   <li>Если данные некорректны или введены ошибки, пользователю выводится соответствующее сообщение.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды. Ожидается один аргумент — ключ транспортного средства, которое требуется обновить.
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("update");

        if (args.length != 1) {
            System.out.println("Данная команда должна содержать 1 аргумент(ключ элемента).");
            return;
        }
        
        try {
            Long key = Long.parseLong(args[0]);

            if (VehicleCollection.getVehicle().containsKey(key)) {
                VehicleCollection.getVehicle().remove(key);

                try {
                    while (true) {

                        InputReader inputReader = new InputReader();
                        
                        String name = null;
                        while (name == null || name.trim().isEmpty()) {
                            System.out.print("Введите название транспорта (не может быть пустым): ");
                            name = inputReader.readString().trim();
                            if (name.isEmpty()) {
                                System.out.println("Ошибка: Название транспорта не может быть пустым!");
                            }
                        }

                        Long x = null;
                        while (x == null || x <= -978) {
                            System.out.print("Введите координату X (целое число, минимумм -978): ");
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
                        Long y = null;
                        while (y == null) {
                            System.out.print("Введите координату Y (целое число, минимумм -45) или оставьте пустым: ");
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

                        Long enginePower = null;
                        while (enginePower == null) {
                            System.out.print("Введите силу двигателя (целое число, минимумм 1) или оставьте пустым: ");
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
                        
                        Float fuelConsumption = null;
                        while (fuelConsumption == null || x <= 0) {
                            System.out.print("Введите потребление топлива (целое число, минимумм 1): ");
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
                     
                     Long distanceTravelled = null;
                     while (distanceTravelled == null || x <= 0) {
                         System.out.print("Введите пройденную дистанцию (целое число, минимумм 1): ");
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
            } else {
                System.out.println("Ошибка: Элемента с таким ключом не существует.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть целым числом.");
        }
    }
}