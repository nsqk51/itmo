package server.ProgrammManagment;

import java.util.HashMap;
import java.util.Map;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

/**
 * Команда для отображения всех транспортных средств в коллекции.
 * <p>
 * Эта команда выводит строковое представление каждого транспортного средства из коллекции {@code VehicleCollection}.
 * Коллекция сортируется перед выводом.
 * </p>
 */
public class CommandShow extends Command {

    /**
     * Выполняет команду отображения всех транспортных средств в коллекции.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Проверяет, что команда не содержит аргументов.</li>
     *   <li>Если аргументы отсутствуют, выводит строковое представление всех транспортных средств в коллекции.</li>
     *   <li>Если аргументы есть, выводит сообщение об ошибке, что команда не должна содержать аргументов.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды. Должны отсутствовать, иначе будет выведено сообщение об ошибке.
     */
  @Override
  public String execute(String[] args) {
      if (args.length != 0) {
          return "Данная команда не должна содержать аргументов.";
      } else {
          StringBuilder result = new StringBuilder();

          // Получаем полную коллекцию с ключами
          var movieMap = VehicleCollection.getVehicle(); // Hashtable<Long, Movie>

          if (movieMap.isEmpty()) {
              return "Коллекция пуста.";
          }

          // Сортируем значения
          var sortedVehicles = VehicleCollection.getSortList(); // List<Movie>

          for (Vehicle movie : sortedVehicles) {
              // Ищем соответствующий ключ
              Long key = getKeyByValue(movieMap, movie);
              result.append("Ключ: ").append(key).append("\n");
              result.append(movie).append("\n\n");
          }

          return result.toString();
      }
  }

  /**
   * Вспомогательный метод для поиска ключа по значению.
   */
  private Long getKeyByValue(HashMap<Long, Vehicle> map, Vehicle value) {
      for (Map.Entry<Long, Vehicle> entry : map.entrySet()) {
          if (entry.getValue().equals(value)) {
              return entry.getKey();
          }
      }
      return null;
  }

}
