package server.ProgrammManagment;

import java.time.LocalDateTime;

import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.entity.VehicleType;

/**
 * Команда для обновления информации о транспортном средстве в коллекции.
 * <p>
 * Эта команда позволяет обновить данные транспортного средства в коллекции по заданному ID.
 * Вводятся новые значения для полей транспортного средства, таких как имя, координаты, сила двигателя,
 * потребление топлива, дистанция и тип транспорта. Если транспорт с заданным ID найден в коллекции,
 * он будет заменен новыми значениями.
 * </p>
 */
public class CommandUpdate extends Command {

  @Override
    public String execute(String[] args) {
      if (args.length < 8) { 
          return "Ошибка: недостаточно аргументов для создания траснпорта.";
      }
      
        Vehicle vehicleToUpdate = null;
        Long keyToUpdate = null;

        try {
            int id = Integer.parseInt(args[0]);

            for (var entry : VehicleCollection.getVehicle().entrySet()) {
                if (entry.getValue().getId() == id) {
                    vehicleToUpdate = entry.getValue();
                    keyToUpdate = entry.getKey();
                    break;
                }
            }

            if (vehicleToUpdate != null && keyToUpdate != null) {
                VehicleCollection.getVehicle().remove(keyToUpdate);

                {
                  String name = args[1];
                  long x = Long.parseLong(args[2]);
                  Long y = Long.parseLong(args[3]);
                  Long enginePower = Long.parseLong(args[4]);
                  float fuelConsumption = Float.parseFloat(args[5]);
                  long distanceTravelled = Long.parseLong(args[6]);
                  VehicleType type = (args[7].trim().isEmpty()) ? null : parseType(args[7]);

                  Coordinates coordinates = new Coordinates(x, y);
                  Vehicle vehicle = new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);

                  VehicleCollection.getVehicle().put(keyToUpdate, vehicle);
                  
                    return "Транспорт успешно обновлён!";

                } 
            } else {
                return "Ошибка: Элемента с таким ID не существует.";
            } 
        } catch (NullPointerException e) {
          VehicleCollection.getVehicle().put(keyToUpdate, vehicleToUpdate);
            return "\nВвод остановлен.";
        } catch (NumberFormatException e) {
            return "Ошибка: Неверный формат числа.";
        } catch (IllegalArgumentException e) {
            return "Ошибка: Неверное значение для типа транмпорта.";
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