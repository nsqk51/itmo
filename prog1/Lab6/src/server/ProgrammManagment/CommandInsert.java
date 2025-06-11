package server.ProgrammManagment;

import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.entity.VehicleType;

import java.time.LocalDateTime;

public class CommandInsert extends Command {

  @Override
  public String execute(String[] args) {
      if (args.length < 8) { 
          return "Ошибка: недостаточно аргументов для создания транспорта.";
      }

      try {
          Long key = Long.parseLong(args[0]);

          if (VehicleCollection.getVehicle().containsKey(key)) {
              return "Ошибка: Элемент с таким ключом уже существует.";
          }

          String name = args[1];
          long x = Long.parseLong(args[2]);
          Long y = Long.parseLong(args[3]);
          Long enginePower = Long.parseLong(args[4]);
          float fuelConsumption = Float.parseFloat(args[5]);
          long distanceTravelled = Long.parseLong(args[6]);
          VehicleType type = (args[7].trim().isEmpty()) ? null : parseType(args[7]);

          Coordinates coordinates = new Coordinates(x, y);
          Vehicle vehicle = new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);

          VehicleCollection.getVehicle().put(key, vehicle);

          return "Транспорт успешно добавлен в коллекцию!";


      }  catch (NumberFormatException e) {
          return "Ошибка: Неверный формат числа.";
      } catch (IllegalArgumentException e) {
          return "Ошибка: Неверное значение для типа транспорта.";
      } catch (Exception e) {
          return "Произошла ошибка при создании транспорта: " + e.getMessage();
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