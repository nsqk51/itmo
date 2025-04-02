package ProgrammManagment;

import entity.Vehicle;
import entity.VehicleCollection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода всех транспортных средств, отсортированных по возрастанию силы двигателя.
 * <p>
 * Эта команда извлекает транспортные средства из коллекции и сортирует их по силе двигателя.
 * Результат выводится на экран в порядке возрастания значения силы двигателя.
 * </p>
 */
public class CommandPrintFieldAscendingEnginePower extends Command {

    /**
     * Выполняет команду, выводя список транспортных средств, отсортированных по силе двигателя.
     * <p>
     * Метод собирает все транспортные средства в список, фильтрует те, у которых сила двигателя равна null,
     * сортирует их по возрастанию силы двигателя и выводит результат на экран.
     * </p>
     *
     * @param args Аргументы команды. Для этой команды аргументы не требуются.
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("print_field_ascending_engine_power");

        // Получаем список всех транспортных средств из коллекции
        List<Vehicle> vehicles = VehicleCollection.getVehicle().values().stream()
                .collect(Collectors.toList());

        // Сортируем транспортные средства по возрастанию силы двигателя и выводим их
        vehicles.stream()
                .map(Vehicle::getEnginePower)
                .filter(enginePower -> enginePower != null)  // Исключаем транспортные средства с null значением силы двигателя
                .sorted()  // Сортировка по возрастанию
                .forEach(System.out::println);  // Выводим каждый элемент
    }
}