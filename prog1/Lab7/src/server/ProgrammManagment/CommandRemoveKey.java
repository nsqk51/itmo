package server.ProgrammManagment;

import java.sql.SQLException;
import java.util.Map;

import server.Server;
import server.entity.Vehicle;
import server.entity.VehicleCollection;

/**
 * Команда для удаления фильма из коллекции по ключу.
 * <p>
 * Этот класс реализует команду, которая удаляет фильм из коллекции по заданному ключу. Если элемента с таким ключом
 * не существует, выводится соответствующее сообщение.
 * </p>
 */
public class CommandRemoveKey extends Command {

    /**
     * Выполняет команду удаления фильма по ключу.
     * <p>
     * Проверяет, что команда принимает 1 аргумент (ключ элемента), затем пытается удалить фильм с указанным ключом.
     * Если фильм с таким ключом существует в коллекции, он будет удален. Если элемента с таким ключом нет, будет выведено
     * сообщение об ошибке.
     * </p>
     *
     * @param args Аргументы команды. Ожидается один аргумент: строка, представляющая ключ фильма, который необходимо удалить.
     */
	Vehicle movieToUpdate;
	
	@Override
	public Boolean checkUser(String login, String password) {
	    return Server.allUsers.authenticate(login, password);
	}
	
	@Override
	public String execute(String[] args, String login, String password) {
	    if (args.length != 1) {
	        return "Данная команда должна содержать 1 аргумент (ключ элемента).";
	    }

	    try {
	        Long key = Long.parseLong(args[0]);

	        // Получаем коллекцию
	        Map<Long, Vehicle> movies = VehicleCollection.getVehicle();

	        // Проверяем, существует ли элемент с таким ключом
	        if (!movies.containsKey(key)) {
	            return "Ошибка: Элемента с таким ключом не существует.";
	        }

	        Vehicle vehicleToRemove = movies.get(key);

	        // Проверяем, что пользователь имеет право удалить этот фильм
	        if (!vehicleToRemove.getCreatedBy().equals(login) || !checkUser(login, password)) {
	            return "Ошибка: У вас нет прав на удаление этого фильма.";
	        }

	        // Удаляем из базы данных
	        try {
	            Server.vehicleManager.deleteVehicle(vehicleToRemove.getId());
	            movies.remove(key);  // Удаляем из коллекции по ключу
	            return "Элемент с ключом " + key + " успешно удален.";
	        } catch (SQLException e) {
	            return "Ошибка при удалении из БД: " + e.getMessage();
	        }

	    } catch (NumberFormatException e) {
	        return "Ошибка: Ключ должен быть числом.";
	    }
	}

}