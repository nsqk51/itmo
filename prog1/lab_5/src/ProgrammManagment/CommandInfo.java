package ProgrammManagment;

import entity.VehicleCollection;

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
    public void execute(String[] args) {
        CommandHistory.addCommand("info");

        if (args.length != 0) {
            System.out.println("Данная команда не должна содержать аргументов.");
        } else {
            System.out.println("Информация о коллекции:");
            System.out.println("Дата инициализации: " + VehicleCollection.getInitializationdate());
            System.out.println("Тип коллекции: " + VehicleCollection.vehicles.getClass().getSimpleName());
            System.out.println("Количество элементов: " + VehicleCollection.vehicles.size());
        }
    }
}