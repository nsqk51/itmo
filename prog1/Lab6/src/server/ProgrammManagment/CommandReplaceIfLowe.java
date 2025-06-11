package server.ProgrammManagment;

import java.time.LocalDateTime;

import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleCollection;
import server.entity.VehicleType;

/**
 * Команда для замены элемента коллекции, если новое значение меньше текущего.
 * <p>
 * Эта команда позволяет заменить элемент в коллекции на новый, если его мощность двигателя меньше, чем у
 * уже существующего элемента с данным ключом. Для этого команда вызывает команду обновления элемента,
 * а затем проверяет, если новое значение меньше текущего, производит замену. Если новое значение не меньше,
 * замена отменяется.
 * </p>
 */
public class CommandReplaceIfLowe extends Command {

    private final CommandUpdate commandUpdate;

    /**
     * Конструктор класса.
     * <p>
     * Создает команду замены, передавая команду обновления для использования в процессе замены.
     * </p>
     *
     * @param commandUpdate Объект команды для обновления данных транспортного средства.
     */
    public CommandReplaceIfLowe(CommandUpdate commandUpdate) {
        this.commandUpdate = commandUpdate;
    }

    /**
     * Выполняет команду замены элемента коллекции, если новое значение меньше текущего.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Запрашивает ключ элемента для замены.</li>
     *   <li>Проверяет наличие элемента с данным ключом в коллекции.</li>
     *   <li>Запрашивает данные для обновления транспортного средства.</li>
     *   <li>Сравнивает мощность двигателя текущего и обновленного элемента.</li>
     *   <li>Если новое значение меньше текущего, элемент заменяется; если нет — замена отменяется.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды: 1 аргумент (ключ элемента, который требуется заменить).
     */
    @Override
    public String execute(String[] args) {
        CommandHistory.addCommand("replace_if_lowe");

        if (args.length < 7) {
            return "Ошибка: недостаточно аргументов.";
        }

        try {
            Long key = Long.parseLong(args[0]);

            if (!VehicleCollection.getVehicle().containsKey(key)) {
                return "Ошибка: Элемента с ключом '" + key + "' не существует.";
            }

            Vehicle existingVehicle = VehicleCollection.getVehicle().get(key);
            Long currentEnginePower = existingVehicle.getEnginePower();

            String name = args[1];
          long x = Long.parseLong(args[2]);
          Long y = Long.parseLong(args[3]);
          Long enginePower = Long.parseLong(args[4]);
          float fuelConsumption = Float.parseFloat(args[5]);
          long distanceTravelled = Long.parseLong(args[6]);
          VehicleType type = (args[7].trim().isEmpty()) ? null : parseType(args[7]);

          Coordinates coordinates = new Coordinates(x, y);
          Vehicle newVehicle = new Vehicle(name, coordinates, LocalDateTime.now(), enginePower, fuelConsumption, distanceTravelled, type);
            
            if (enginePower < currentEnginePower) {
              VehicleCollection.getVehicle().put(key, newVehicle);
                return "Замена успешно выполнена! Новое значение меньше старого.";
            } else {
                return "Отмена замены: новое значение (" + enginePower +
                        ") не меньше текущего (" + currentEnginePower + ").";
            }

        } catch (NumberFormatException e) {
            return "Ошибка: Ключ должен быть числом.";
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