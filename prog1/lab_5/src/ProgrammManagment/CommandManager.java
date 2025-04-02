package ProgrammManagment;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер команд. Содержит мапу команд и управляет их выполнением.
 */
public class CommandManager {
    
    private Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор, инициализирует все доступные команды.
     */
    public CommandManager() {
        // Регистрация команд
        commands.put("help", new CommandHelp(commands)); 
        commands.put("info", new CommandInfo());
        commands.put("show", new CommandShow());
        commands.put("insert", new CommandInsert());
        commands.put("update", new CommandUpdate());
        commands.put("remove_key", new CommandRemoveKey());
        commands.put("clear", new CommandClear());
        commands.put("save", new CommandSave());
        commands.put("execute_script", new CommandExecuteScript(this));
        commands.put("exit", new CommandExit());
        commands.put("remove_lower", new CommandRemoveLower());
        commands.put("history", new CommandHistory());
        commands.put("replace_if_lowe", new CommandReplaceIfLowe(new CommandUpdate()));
        commands.put("min_by_coordinates", new CommandMinByCoordinates());
        commands.put("filter_starts_with_name", new CommandFilterStartsWithName());
        commands.put("print_field_ascending_engine_power", new CommandPrintFieldAscendingEnginePower());
    }

    /**
     * Выполняет команду по её имени, передавая параметры.
     *
     * @param commandName имя команды.
     * @param args аргументы для команды.
     * @return true, если команда была выполнена, иначе false.
     */
    public boolean executeCommand(String commandName, String[] args) {
        Command command = commands.get(commandName);
        
        if (command != null) {
            command.execute(args);
            return true;
        } else {
            System.out.println("Команда не найдена!");
            return false;
        }
    }
}