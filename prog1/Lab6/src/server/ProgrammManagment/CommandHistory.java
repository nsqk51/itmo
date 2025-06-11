package server.ProgrammManagment;

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
    public String execute(String[] args) {
        CommandHistory.addCommand("history");

        if (args.length != 0) {
            return "Ошибка: Команда 'history' не требует аргументов.";
        }

        if (history.isEmpty()) {
            return "История команд пуста.";
        }

        StringBuilder result = new StringBuilder("Последние " + history.size() + " команд:\n");
        for (String command : history) {
            result.append("  ").append(command).append("\n");
        }

        return result.toString().trim();
    }
}