package ProgrammManagment;

import java.util.Map;

/**
 * Команда для вывода списка доступных команд.
 */
public class CommandHelp extends Command {
    private final Map<String, Command> commands;

    /**
     * Конструктор для команды help.
     *
     * @param commands карта, содержащая доступные команды.
     */
    public CommandHelp(Map<String, Command> commands) {
        this.commands = commands;
    }

    /**
     * Выполняет команду помощи.
     * Выводит список доступных команд.
     *
     * @param args аргументы команды (не должны быть предоставлены).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("help");

        if (args.length != 0) {
            System.out.println("Данная команда не должна содержать аргументов.");
        } else {
            System.out.println("Доступные команды:");
            for (String command : commands.keySet()) {
                System.out.println(" - " + command);
            }
        }
    }
}