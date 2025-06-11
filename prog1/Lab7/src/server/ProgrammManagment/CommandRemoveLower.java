package server.ProgrammManagment;

import server.Server;
import server.entity.Vehicle;
import server.entity.VehicleCollection;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * Команда для удаления элементов коллекции с меньшей мощностью двигателя.
 * <p>
 * Эта команда позволяет удалить все транспортные средства из коллекции, у которых мощность двигателя меньше,
 * чем у указанного транспортного средства. Удаляются только те элементы, владельцем которых является текущий пользователь.
 * </p>
 */
public class CommandRemoveLower extends Command {

    @Override
    public Boolean checkUser(String login, String password) {
        return Server.allUsers.authenticate(login, password);
    }

    @Override
    public String execute(String[] args, String login, String password) {
        CommandHistory.addCommand("remove_lower");

        if (!checkUser(login, password)) {
            return "Ошибка: неверные логин или пароль.";
        }

        if (args.length < 8) {
            return "Ошибка: недостаточно аргументов.";
        }

        if (args[4] == null) {
            return "Ошибка: У данного транспорта не указана мощность двигателя, удаление невозможно.";
        }

        Long enginePower;
        try {
            enginePower = Long.parseLong(args[4]);
        } catch (NumberFormatException e) {
            return "Ошибка: мощность двигателя должна быть числом.";
        }

        Iterator<Map.Entry<Long, Vehicle>> iterator = VehicleCollection.getVehicle().entrySet().iterator();
        int removedCount = 0;

        while (iterator.hasNext()) {
            Map.Entry<Long, Vehicle> entry = iterator.next();
            Vehicle vehicle = entry.getValue();

            if (vehicle.getEnginePower() != null && vehicle.getEnginePower() < enginePower
                    && login.equals(vehicle.getCreatedBy())) {
                try {
                    Server.vehicleManager.deleteVehicle(vehicle.getId());
                    iterator.remove();
                    removedCount++;
                } catch (SQLException e) {
                    return "Ошибка при удалении из базы данных: " + e.getMessage();
                }
            }
        }

        return "Удалено элементов: " + removedCount;
    }
}
