package ProgrammManagment;

import entity.Vehicle;
import entity.VehicleCollection;

import java.util.Comparator;
import java.util.Optional;

/**
 * Команда для нахождения транспортного средства с минимальными координатами.
 * <p>
 * Данная команда находит транспортное средство с минимальными значениями координат X и Y
 * из всех элементов коллекции. Сначала осуществляется сравнение по координате X, затем, если
 * они равны, по координате Y.
 * </p>
 */
public class CommandMinByCoordinates extends Command {

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
    public void execute(String[] args) {
        CommandHistory.addCommand("min_by_coordinates");

        if (VehicleCollection.getVehicle().isEmpty()) {
            System.out.println("Ошибка: Коллекция пуста.");
            return;
        }

        Optional<Vehicle> minVehicle = VehicleCollection.getVehicle().values().stream()
                .min(Comparator.comparingLong(v -> ((Vehicle) v).getCoordinates().getX())
                        .thenComparingLong(v -> ((Vehicle) v).getCoordinates().getY()));

        minVehicle.ifPresentOrElse(
                vehicle -> System.out.println("Объект с минимальными координатами:\n" + vehicle),
                () -> System.out.println("Ошибка: Не удалось найти объект с минимальными координатами.")
        );
    }
}