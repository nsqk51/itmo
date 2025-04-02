package ProgrammManagment;

import entity.Vehicle;
import entity.VehicleCollection;

/**
 * Команда для отображения всех транспортных средств в коллекции.
 * <p>
 * Эта команда выводит строковое представление каждого транспортного средства из коллекции {@code VehicleCollection}.
 * Коллекция сортируется перед выводом.
 * </p>
 */
public class CommandShow extends Command {

    /**
     * Выполняет команду отображения всех транспортных средств в коллекции.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Проверяет, что команда не содержит аргументов.</li>
     *   <li>Если аргументы отсутствуют, выводит строковое представление всех транспортных средств в коллекции.</li>
     *   <li>Если аргументы есть, выводит сообщение об ошибке, что команда не должна содержать аргументов.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды. Должны отсутствовать, иначе будет выведено сообщение об ошибке.
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("show");

        if (args.length != 0) {
            System.out.println("Данная команда не должна содержать аргументов.");
        } else {
            System.out.println("Cтроковое представление каждого транспортного средства: ");
            for (Vehicle vehicle : VehicleCollection.getSortList()) {
                System.out.println(vehicle.toString());
            }
        }
    }
}
