package server.ProgrammManagment;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Команда для отображения всех фильмов в коллекции.
 * <p>
 * Этот класс реализует команду, которая выводит строковое представление каждого фильма в коллекции
 * вместе с его ключом.
 * </p>
 */
public class CommandShow extends Command {

	@Override 
    public Boolean checkUser(String login, String password){
		return null;
    }
	
    @Override
    public String execute(String[] args, String login, String password) {
        if (args.length != 0) {
            return "Данная команда не должна содержать аргументов.";
        }

        HashMap<Long, Vehicle> movieMap = VehicleCollection.getVehicle();

        if (movieMap.isEmpty()) {
            return "Коллекция пуста.";
        }

        StringBuilder result = new StringBuilder();
        for (Vehicle vehicle : VehicleCollection.getSortList()) {
            Long key = getKeyByValue(movieMap, vehicle);
            result.append("Ключ: ").append(key).append("\n");
            result.append(vehicle).append("\n\n");
        }

        return result.toString();
    }

    /**
     * Вспомогательный метод для поиска ключа по значению.
     */
    private Long getKeyByValue(HashMap<Long, Vehicle> map, Vehicle value) {
        for (Map.Entry<Long, Vehicle> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
