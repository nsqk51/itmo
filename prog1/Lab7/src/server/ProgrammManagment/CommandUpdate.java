package server.ProgrammManagment;

import java.sql.SQLException;
import java.time.LocalDateTime;

import server.Server;
import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.entity.VehicleType;

public class CommandUpdate extends Command {

    @Override
    public Boolean checkUser(String login, String password) {
        return Server.allUsers.authenticate(login, password);
    }

    @Override
    public String execute(String[] args, String login, String password) {
        if (args.length < 8) {
            return "Ошибка: недостаточно аргументов для обновления транспорта.";
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return "Ошибка: ID должен быть числом.";
        }

        Vehicle vehicleToUpdate = null;
        Long keyToUpdate = null;

        // Поиск по id
        for (var entry : VehicleCollection.getVehicle().entrySet()) {
            if (entry.getValue().getId() == id) {
                vehicleToUpdate = entry.getValue();
                keyToUpdate = entry.getKey();
                break;
            }
        }

        if (vehicleToUpdate == null || keyToUpdate == null) {
            return "Ошибка: Элемента с таким ID не существует.";
        }

        // Проверка прав пользователя
        if (!vehicleToUpdate.getCreatedBy().equals(login) || !checkUser(login, password)) {
            return "Ошибка: У вас нет прав на изменение этого транспорта.";
        }

        try {
            String name = args[1];
            long x = Long.parseLong(args[2]);
            Long y = Long.parseLong(args[3]);
            Long enginePower = Long.parseLong(args[4]);
            float fuelConsumption = Float.parseFloat(args[5]);
            long distanceTravelled = Long.parseLong(args[6]);
            VehicleType type = (args[7].trim().isEmpty()) ? null : parseType(args[7]);

            Coordinates coordinates = new Coordinates(x, y);
            Vehicle updatedVehicle = new Vehicle(id, name, coordinates, LocalDateTime.now(), enginePower,
                    fuelConsumption, distanceTravelled, type, login);

            // Обновление в БД
            try {
                Server.vehicleManager.updateVehicle(id, updatedVehicle);
            } catch (SQLException e) {
                return "Ошибка при обновлении записи в БД: " + e.getMessage();
            }

            // Обновление в коллекции
            VehicleCollection.getVehicle().put(keyToUpdate, updatedVehicle);

            return "Транспорт успешно обновлён.";
        } catch (NumberFormatException e) {
            return "Ошибка: Неверный формат числового значения.";
        } catch (IllegalArgumentException e) {
            return "Ошибка: Неверное значение для типа транспорта.";
        } catch (Exception e) {
            return "Произошла ошибка при обновлении транспорта: " + e.getMessage();
        }
    }

    private VehicleType parseType(String typeString) {
        try {
            if (typeString == null || typeString.trim().isEmpty()) {
                return null;
            }
            return VehicleType.valueOf(typeString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
