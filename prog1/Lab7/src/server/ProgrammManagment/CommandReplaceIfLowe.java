package server.ProgrammManagment;

import java.sql.SQLException;
import java.time.LocalDateTime;

import server.Server;
import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.entity.VehicleType;

/**
 * Команда для замены элемента коллекции, если новое значение меньше текущего.
 * <p>
 * Эта команда позволяет заменить элемент в коллекции на новый, если его мощность двигателя меньше, чем у
 * уже существующего элемента с данным ключом. Замена возможна только владельцем объекта.
 * </p>
 */
public class CommandReplaceIfLowe extends Command {

    private final CommandUpdate commandUpdate;

    public CommandReplaceIfLowe(CommandUpdate commandUpdate) {
        this.commandUpdate = commandUpdate;
    }

    @Override
    public Boolean checkUser(String login, String password) {
        return Server.allUsers.authenticate(login, password);
    }

    @Override
    public String execute(String[] args, String login, String password) {
        CommandHistory.addCommand("replace_if_lowe");

        if (!checkUser(login, password)) {
            return "Ошибка: неверные логин или пароль.";
        }

        if (args.length < 8) {
            return "Ошибка: недостаточно аргументов.";
        }

        try {
            Long key = Long.parseLong(args[0]);

            if (!VehicleCollection.getVehicle().containsKey(key)) {
                return "Ошибка: Элемента с ключом '" + key + "' не существует.";
            }

            Vehicle existingVehicle = VehicleCollection.getVehicle().get(key);

            // Проверка владельца
            if (!login.equals(existingVehicle.getCreatedBy())) {
                return "Ошибка: у вас нет прав на замену этого элемента.";
            }

            String name = args[1];
            long x = Long.parseLong(args[2]);
            Long y = Long.parseLong(args[3]);
            Long enginePower = Long.parseLong(args[4]);
            float fuelConsumption = Float.parseFloat(args[5]);
            long distanceTravelled = Long.parseLong(args[6]);
            VehicleType type = (args[7].trim().isEmpty()) ? null : parseType(args[7]);

            Coordinates coordinates = new Coordinates(x, y);
            Vehicle newVehicle = new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type, login);
            newVehicle.setId(existingVehicle.getId()); // сохраняем ID
            newVehicle.setCreatedBy(login); // сохраняем владельца

            if (enginePower < existingVehicle.getEnginePower()) {
                try {
                    Server.vehicleManager.updateVehicle(existingVehicle.getId(), newVehicle);
                    VehicleCollection.getVehicle().put(key, newVehicle);
                    return "Замена успешно выполнена! Новое значение меньше старого.";
                } catch (SQLException e) {
                    return "Ошибка при обновлении БД: " + e.getMessage();
                }
            } else {
                return "Отмена замены: новое значение (" + enginePower +
                        ") не меньше текущего (" + existingVehicle.getEnginePower() + ").";
            }

        } catch (NumberFormatException e) {
            return "Ошибка: неверный формат числового значения.";
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
