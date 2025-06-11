package server.ProgrammManagment;

import server.entity.VehicleCollection;

/**
 * Команда для очистки коллекции транспортных средств.
 * <p>
 * Удаляет все элементы из коллекции {@link VehicleCollection}.
 * </p>
 */
public class CommandClear extends Command {

    /**
     * Выполняет команду очистки коллекции.
     * <p>
     * Если команда получает аргументы, выводится сообщение об ошибке.
     * В противном случае коллекция полностью очищается.
     * </p>
     *
     * @param args массив аргументов команды (ожидается пустой)
     */
    @Override
    public String execute(String[] args) {
        CommandHistory.addCommand("clear");

        if (args.length != 0) {
            return "Данная команда не должна содержать аргументов.";
        } else {
            VehicleCollection.vehicles.clear();
            return "Коллекция очищена.";
        }
    }
}