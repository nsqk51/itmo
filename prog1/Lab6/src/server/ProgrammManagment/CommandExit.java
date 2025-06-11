package server.ProgrammManagment;

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
    public String execute(String[] args) {
        CommandHistory.addCommand("exit");

        if (args.length != 0) {
            return "Ошибка: Команда 'exit' не должна содержать аргументов.";
        } else {
            CommandSave save = new CommandSave();
      save.execute(args);

      return "Клиент завершает работу. Коллекция фильмов сохранена.";
        }
    }
}
