package server.ProgrammManagment;

import java.util.Map;

public class ExecuteCommand {
	static CommandManager commandManager = new CommandManager();
	static Map<String, Command> commands = commandManager.getCommands();
    @SuppressWarnings("unchecked")
    public static String executeCommand(String commandName, String[] args, String login, String password) {
        Command command = commands.get(commandName);

        if (command == null) {
            return "Ошибка: Команда не найдена.";
        }

        try {
            Command typedCommand = (Command) command;
            return typedCommand.execute(args, login, password);
        } catch (ClassCastException e) {
            return "Ошибка приведения типов: " + e.getMessage();
        }  catch (NullPointerException e) {
            return "Остановка работы.";
        } catch (Exception e) {
            return "Ошибка выполнения команды: " + e.getMessage();
        }
        
    }
}

