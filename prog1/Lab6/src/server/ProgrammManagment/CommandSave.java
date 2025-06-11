package server.ProgrammManagment;

import java.io.File;

import server.Sup.ParseToJson;

/**
 * Команда для сохранения коллекции транспортных средств в файл.
 * <p>
 * Эта команда сохраняет текущую коллекцию транспортных средств в файл в формате JSON. Для этого используется
 * утилитный класс `Parse_to_Json`, который осуществляет запись данных в файл по пути, указанному в
 * переменной окружения {@code FILE_WITH_VEHICLE}.
 * </p>
 */
public class CommandSave extends Command {

    /**
     * Выполняет команду сохранения коллекции транспортных средств в файл.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Проверяет, что команда не содержит аргументов.</li>
     *   <li>Если аргументы отсутствуют, вызывает метод для записи коллекции в файл.</li>
     *   <li>Если аргументы есть, выводит ошибку о том, что команда не должна содержать аргументов.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды. Должны отсутствовать, иначе будет выведено сообщение об ошибке.
     */
  @Override
    public String execute(String[] args) {
        if (args != null && args.length != 0) {
            return "Данная команда не должна содержать аргументов.";
        }

        try {
            String fileName = System.getenv("FILE_WITH_VEHICLE");
            if (fileName == null || fileName.isEmpty()) {
                return "Ошибка: переменная окружения FILE_WITH_VEHICLE не установлена.";
            }

            File file = new File("Files", fileName); // тот же путь, что при чтении
            ParseToJson.writeVehiclesToFile(file.getAbsolutePath());

            return "Коллекция транспорта успешно сохранена в файл: " + file.getAbsolutePath();
        } catch (Exception e) {
            return "Ошибка при сохранении коллекции в файл: " + e.getMessage();
        }
    }
}