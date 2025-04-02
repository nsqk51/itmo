package ProgrammManagment;

/**
 * Команда для завершения работы программы.
 * <p>
 * Завершает выполнение программы при вызове.
 * </p>
 */
public class CommandExit extends Command {

    /**
     * Выполняет команду завершения программы.
     * 
     * @param args аргументы команды (не должны передаваться).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("exit");

        if (args.length != 0) {
            System.out.println("Ошибка: Команда 'exit' не должна содержать аргументов.");
            return;
        }

        System.out.println("Программа завершена.");
        System.exit(0);
    }
}