package server.ProgrammManagment;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import server.Server;
import server.entity.Vehicle;
import server.entity.VehicleCollection;

/**
 * Класс, представляющий команду для очистки коллекции фильмов.
 * <p>
 * Этот класс наследует от {@link Command} и реализует метод {@link Command#execute(String[])}.
 * Команда очищает коллекцию фильмов, удаляя все элементы из хранилища {@link MovieCollection#movies}.
 * </p>
 */
public class CommandClear extends Command {

    /**
     * Выполняет очистку коллекции фильмов.
     * <p>
     * Этот метод удаляет все элементы из коллекции {@link MovieCollection#movies}.
     * Если метод был вызван с аргументами, выводится ошибка, так как команда не должна принимать аргументы.
     * </p>
     * 
     * @param args Массив строковых аргументов, переданных командой. Этот массив не должен содержать аргументов.
     */
	Vehicle vehicleToUpdate;
	
	@Override
	public Boolean checkUser(String login, String password) {
	    return Server.allUsers.authenticate(login, password);
	}
	
	@Override
	public String execute(String[] args, String login, String password) {
	    if (args.length != 0) {
	        return "Данная команда не должна содержать аргументов.";
	    } else {
	        // Получаем итератор по значениям (объектам Movie)
	        Iterator<Map.Entry<Long, Vehicle>> iterator = VehicleCollection.vehicles.entrySet().iterator();
	        while (iterator.hasNext()) {
	            Map.Entry<Long, Vehicle> entry = iterator.next();
	            Vehicle vehicleToClear = entry.getValue();

	            if (vehicleToClear.getCreatedBy().equals(login) && checkUser(login, password)) {
	                try {
	                    Server.vehicleManager.deleteVehicle(vehicleToClear.getId());
	                    iterator.remove(); // безопасное удаление
	                } catch (SQLException e) {
	                    return "Ошибка при удалении из БД: " + e.getMessage();
	                }
	            }
	        }
	        return "Коллекция очищена.";
	    }
	}

}