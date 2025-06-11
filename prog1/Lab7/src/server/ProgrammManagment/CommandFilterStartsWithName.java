package server.ProgrammManagment;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда для вывода элементов, значение поля name которых начинается с заданной подстроки.
 */
public class CommandFilterStartsWithName extends Command {

	
	@Override 
    public Boolean checkUser(String login, String password){
		return null;
    }
    /**
     * Выполняет команду фильтрации по имени.
     *
     * @param args аргументы команды (должен быть один аргумент - подстрока для поиска).
     * @return строковое представление найденных объектов или сообщение об отсутствии совпадений.
     */
    @Override
    public String execute(String[] args, String login, String password) {
        CommandHistory.addCommand("filter_starts_with_name");

        if (args.length != 1 || args[0].trim().isEmpty()) {
            return "Ошибка: Команда 'filter_starts_with_name' требует 1 аргумент (подстроку для поиска).";
        }

        String searchString = args[0].trim();
        List<Vehicle> matchingVehicles = new ArrayList<>();

        for (Vehicle vehicle : VehicleCollection.getVehicle().values()) {
            if (vehicle.getName().startsWith(searchString)) {
                matchingVehicles.add(vehicle);
            }
        }

        if (matchingVehicles.isEmpty()) {
            return "Нет элементов, имя которых начинается с '" + searchString + "'.";
        }

        StringBuilder result = new StringBuilder("Найденные элементы:\n");
        for (Vehicle vehicle : matchingVehicles) {
            result.append(vehicle.toString()).append("\n\n");
        }

        return result.toString().trim();
    }
}
