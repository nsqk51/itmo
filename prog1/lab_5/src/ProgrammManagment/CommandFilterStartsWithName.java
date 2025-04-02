package ProgrammManagment;

import entity.Vehicle;
import entity.VehicleCollection;

/**
 * Команда для вывода элементов, значение поля name которых начинается с заданной подстроки.
 */
public class CommandFilterStartsWithName extends Command {

    /**
     * Выполняет команду фильтрации по имени.
     *
     * @param args аргументы команды (должен быть один аргумент - подстрока для поиска).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("filter_starts_with_name");

        if (args.length != 1 || args[0].trim().isEmpty()) {
            System.out.println("Ошибка: Команда 'filter_starts_with_name' требует 1 аргумент (подстроку для поиска).");
            return;
        }

        String searchString = args[0];

        boolean found = false;
        for (Vehicle vehicle : VehicleCollection.getVehicle().values()) {
            if (vehicle.getName().startsWith(searchString)) {
                System.out.println(vehicle);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Нет элементов, имя которых начинается с '" + searchString + "'.");
        }
    }
}