package ProgrammManagment;

import entity.Coordinates;
import entity.Vehicle;
import entity.VehicleCollection;
import entity.VehicleType;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Команда для удаления элементов коллекции с меньшей мощностью двигателя.
 * <p>
 * Эта команда позволяет удалить все транспортные средства из коллекции, у которых мощность двигателя меньше,
 * чем у указанного транспортного средства. Для выполнения команды пользователь должен ввести данные нового транспорта.
 * Если введенные данные корректны, будут удалены все элементы с меньшей мощностью двигателя.
 * </p>
 */
public class CommandRemoveLower extends Command {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Выполняет команду удаления элементов с меньшей мощностью двигателя.
     * <p>
     * Пользователю предлагается ввести данные нового транспорта. Если данные корректны, производится
     * удаление всех элементов с меньшей мощностью двигателя.
     * </p>
     *
     * @param args Аргументы команды. В данной команде они не используются.
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("remove_lower");

        System.out.println("Введите данные нового транспорта:");

        Vehicle vehicle = createVehicleFromInput();
        if (vehicle == null) {
            System.out.println("Ошибка: Некорректные данные, операция отменена.");
            return;
        }

        Long enginePower = vehicle.getEnginePower();
        if (enginePower == null) {
            System.out.println("Ошибка: У данного транспорта не указана мощность двигателя, удаление невозможно.");
            return;
        }

        Iterator<Map.Entry<Long, Vehicle>> iterator = VehicleCollection.getVehicle().entrySet().iterator();
        int removedCount = 0;

        while (iterator.hasNext()) {
            Map.Entry<Long, Vehicle> entry = iterator.next();
            Vehicle existingVehicle = entry.getValue();

            if (existingVehicle.getEnginePower() != null && existingVehicle.getEnginePower() < enginePower) {
                iterator.remove();
                removedCount++;
            }
        }

        System.out.println("Удалено элементов: " + removedCount);
    }

    /**
     * Создает транспортное средство на основе данных, введенных пользователем.
     * <p>
     * Пользователю предлагается ввести название, координаты, мощность двигателя, потребление топлива,
     * пройденную дистанцию и тип транспорта. Если введены некорректные данные, метод возвращает null.
     * </p>
     *
     * @return Новый объект транспортного средства или null, если введены некорректные данные.
     */
    private Vehicle createVehicleFromInput() {
        try {
            System.out.print("Название: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Ошибка: Название не может быть пустым!");
                return null;
            }

            System.out.print("Координата X: ");
            Long x = Long.parseLong(scanner.nextLine().trim());
            if (x <= -978) {
                System.out.println("Ошибка: X должен быть > -978!");
                return null;
            }

            System.out.print("Координата Y: ");
            Long y = Long.parseLong(scanner.nextLine().trim());
            if (y <= -45) {
                System.out.println("Ошибка: Y должен быть > -45!");
                return null;
            }
            Coordinates coordinates = new Coordinates(x, y);

            System.out.print("Сила двигателя: ");
            String enginePowerInput = scanner.nextLine().trim();
            Long enginePower = enginePowerInput.isEmpty() ? null : Long.parseLong(enginePowerInput);
            if (enginePower != null && enginePower <= 0) {
                System.out.println("Ошибка: Сила двигателя должна быть больше 0!");
                return null;
            }
            System.out.print("Потребление топлива: ");
            float fuelConsumption = Float.parseFloat(scanner.nextLine().trim());
            if (fuelConsumption <= 0) {
                System.out.println("Ошибка: Потребление топлива должно быть больше 0!");
                return null;
            }

            System.out.print("Дистанция путешествия: ");
            long distanceTravelled = Long.parseLong(scanner.nextLine().trim());
            if (distanceTravelled <= 0) {
                System.out.println("Ошибка: Дистанция должна быть больше 0!");
                return null;
            }

            System.out.print("Тип транспорта (HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE): ");
            String typeInput = scanner.nextLine().trim().toUpperCase();

            VehicleType type = null;
            if (!typeInput.isEmpty()) {
                try {
                    type = VehicleType.valueOf(typeInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: Неверный тип транспорта! Допустимые значения: HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE.");
                    return null;
                }
            }

            return new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введены некорректные числа!");
            return null;
        }
    }
}