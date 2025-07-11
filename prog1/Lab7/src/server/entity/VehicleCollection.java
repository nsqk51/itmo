package server.entity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Класс для управления коллекцией транспортных средств.
 * <p>
 * Хранит коллекцию {@code vehicles} и предоставляет методы для загрузки, сортировки и генерации уникальных ID.
 * </p>
 */
public class VehicleCollection{
    /** Коллекция транспортных средств, где ключ - ID, значение - объект {@code Vehicle}. */
    public static HashMap<Long, Vehicle> vehicles = new HashMap<>();

    /** Дата и время инициализации коллекции. */
    private static final LocalDateTime DateInitialization = LocalDateTime.now();

    public VehicleCollection(HashMap<Long, Vehicle> vehicles) {
    	VehicleCollection.vehicles = vehicles;
	}
    
    /**
     * Возвращает список транспортных средств, отсортированный по имени.
     *
     * @return Отсортированный список транспортных средств
     */
    public static ArrayList<Vehicle> getSortList() {
        ArrayList<Vehicle> sortList = new ArrayList<>(vehicles.values());
        sortList.sort(Comparator.comparing(Vehicle::getName));

        return sortList;
    }

    /**
     * Возвращает коллекцию транспортных средств.
     *
     * @return Коллекция транспортных средств
     */
    public static HashMap<Long, Vehicle> getVehicle() {
        return vehicles;
    }

    /**
     * Генерирует новый уникальный ID для транспортного средства.
     *
     * @return Новый уникальный ID
     */
    public static long generateId() {
        ArrayList<Vehicle> collection = VehicleCollection.getSortList();
        return collection.stream()
                .mapToLong(Vehicle::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return Дата инициализации
     */
    public static LocalDateTime getInitializationdate() {
        return DateInitialization;
    }
}
