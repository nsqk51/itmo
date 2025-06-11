package server.ProgrammManagment;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

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
    public String execute(String[] args) {
        CommandHistory.addCommand("remove_lower");

        if (args.length < 8) {
            return "Ошибка: недостаточно аргументов.";
        }
        
        if (args[4] == null) {
          return "Ошибка: У данного транспорта не указана мощность двигателя, удаление невозможно.";
        }
        
        Long enginePower = Long.parseLong(args[4]);

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

        return "Удалено элементов: " + removedCount;
    }
}