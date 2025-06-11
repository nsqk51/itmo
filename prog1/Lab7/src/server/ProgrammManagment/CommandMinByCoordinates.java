package server.ProgrammManagment;

import java.util.Comparator;
import java.util.Optional;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

/**
 * Команда для нахождения транспортного средства с минимальными координатами.
 * <p>
 * Данная команда находит транспортное средство с минимальными значениями координат X и Y
 * из всех элементов коллекции. Сначала осуществляется сравнение по координате X, затем, если
 * они равны, по координате Y.
 * </p>
 */
public class CommandMinByCoordinates extends Command {

	@Override 
    public Boolean checkUser(String login, String password){
		return null;
    }
	
    /**
     * Выполняет команду для поиска транспортного средства с минимальными координатами.
     * <p>
     * В случае, если коллекция транспортных средств пуста, выводится сообщение об ошибке.
     * Иначе производится поиск объекта с минимальными координатами с использованием потоков.
     * </p>
     *
     * @param args Аргументы команды. Данная команда не использует аргументы, 
     *             поэтому если они присутствуют, будет выведено сообщение об ошибке.
     */
    @Override
    public String execute(String[] args, String login, String password) {
        CommandHistory.addCommand("min_by_coordinates");

        if (VehicleCollection.getVehicle().isEmpty()) {
            return "Ошибка: Коллекция пуста.";
        }

        Optional<Vehicle> minVehicle = VehicleCollection.getVehicle().values().stream()
                .min(Comparator.comparingLong(v -> ((Vehicle) v).getCoordinates().getX())
                        .thenComparingLong(v -> ((Vehicle) v).getCoordinates().getY()));

        return minVehicle
                .map(vehicle -> "Объект с минимальными координатами:\n" + vehicle)
                .orElse("Ошибка: Не удалось найти объект с минимальными координатами.");
    }
}
