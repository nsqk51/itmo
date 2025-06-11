package server.ProgrammManagment;

import java.util.List;
import java.util.stream.Collectors;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

/**
 * Команда для вывода всех транспортных средств, отсортированных по возрастанию силы двигателя.
 * <p>
 * Эта команда извлекает транспортные средства из коллекции и сортирует их по силе двигателя.
 * Результат выводится на экран в порядке возрастания значения силы двигателя.
 * </p>
 */
public class CommandPrintFieldAscendingEnginePower extends Command {

	@Override 
    public Boolean checkUser(String login, String password){
		return null;
    }
	
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
	public String execute(String[] args, String login, String password) {
        CommandHistory.addCommand("print_field_ascending_engine_power");

        if (args.length != 0) {
            return "Ошибка: Команда 'print_field_ascending_engine_power' не принимает аргументы.";
        }

        List<Long> sortedEnginePowers = VehicleCollection.getVehicle().values().stream()
                .map(Vehicle::getEnginePower)
                .filter(enginePower -> enginePower != null)
                .sorted()
                .collect(Collectors.toList());

        if (sortedEnginePowers.isEmpty()) {
            return "Нет транспортных средств с заданной мощностью двигателя.";
        }

        StringBuilder result = new StringBuilder("Сила двигателя по возрастанию:\n");
        sortedEnginePowers.forEach(power -> result.append(power).append("\n"));
        return result.toString().trim();
    }
}
