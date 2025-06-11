package server.ProgrammManagment;

import server.entity.VehicleCollection;

/**
 * Класс команды 'info'.
 * Выводит информацию о коллекции транспортных средств.
 */
public class CommandInfo extends Command {

    /**
     * Выполняет команду 'info'.
     * Выводит информацию о коллекции транспортных средств: дату инициализации,
     * тип коллекции и количество элементов.
     *
     * @param args аргументы команды (не должны быть предоставлены).
     */
    @Override
    public String execute(String[] args) {
        CommandHistory.addCommand("info");

        if (args.length != 0) {
          return "Данная команда не должна содержать аргументов.";
      } else {
          StringBuilder sb = new StringBuilder();
          sb.append("Информация о коллекции:\n");
          sb.append("Дата инициализации: ").append(VehicleCollection.getInitializationdate()).append("\n");
          sb.append("Тип коллекции: ").append(VehicleCollection.vehicles.getClass().getSimpleName()).append("\n");
          sb.append("Количество элементов: ").append(VehicleCollection.vehicles.size()).append("\n");

          return sb.toString();
      }
    }
}
