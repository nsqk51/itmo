package ProgrammManagment;

import java.util.LinkedList;

/**
 * Класс для управления историей команд.
 * Хранит последние использованные команды и позволяет выводить их.
 */
public class CommandHistory extends Command {
    private static final int HISTORY_SIZE = 15; // Максимальный размер истории команд
    private static final LinkedList<String> history = new LinkedList<>();

    /**
     * Добавляет команду в историю.
     * Если размер истории превышает максимальный, удаляется самая старая команда.
     *
     * @param commandName имя команды, которую необходимо добавить в историю.
     */
    public static void addCommand(String commandName) {
        if (history.size() >= HISTORY_SIZE) {
            history.removeFirst(); // Удаляем самую старую команду, если список переполнен
        }
        history.add(commandName);
    }

    /**
     * Выполняет команду 'history'.
     * Выводит список последних использованных команд.
     *
     * @param args аргументы команды (не должны быть предоставлены).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("history");

        if (args.length != 0) {
            System.out.println("Ошибка: Команда 'history' не требует аргументов.");
            return;
        }

        if (history.isEmpty()) {
            System.out.println("История команд пуста.");
            return;
        }

        System.out.println("Последние " + history.size() + " команд:");
        for (String command : history) {
            System.out.println("  " + command);
        }
    }
}