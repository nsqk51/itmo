package server.ProgrammManagment;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static Map<String, CommandWrapper> commands = new HashMap<>();
    
    static CommandExit exit = new CommandExit();
    static CommandSave save = new CommandSave();
    static CommandInfo info = new CommandInfo();
    static CommandShow show = new CommandShow();
    static CommandClear clear = new CommandClear();
    static CommandRemoveKey removeKey = new CommandRemoveKey();
    static CommandInsert insert = new CommandInsert();
    static CommandUpdate update = new CommandUpdate();
    static CommandRemoveLower removeLower = new CommandRemoveLower();
    static CommandHistory history = new CommandHistory();
    static CommandReplaceIfLowe replaceIfLowe = new CommandReplaceIfLowe(new CommandUpdate());
    static CommandMinByCoordinates minByCoordinates = new CommandMinByCoordinates();
    static CommandFilterStartsWithName filterStartsWithName = new CommandFilterStartsWithName();
    static CommandPrintFieldAscendingEnginePower printFieldAscendingEnginePower = new CommandPrintFieldAscendingEnginePower();
    
    public CommandManager() {
      commands.put("exit", new CommandWrapper(exit, false));
        commands.put("info", new CommandWrapper(info, false));
        commands.put("show", new CommandWrapper(show, false));
        commands.put("clear", new CommandWrapper(clear, false));
        commands.put("remove_key", new CommandWrapper(removeKey, false));
        commands.put("insert", new CommandWrapper(insert, true));
        commands.put("update", new CommandWrapper(update, true)); 
        commands.put("remove_lower", new CommandWrapper(removeLower, true));
        commands.put("history", new CommandWrapper(history, false));
        commands.put("replace_if_lowe", new CommandWrapper(replaceIfLowe, true));
        commands.put("min_by_coordinates", new CommandWrapper(minByCoordinates, false));
        commands.put("filter_starts_with_name", new CommandWrapper(filterStartsWithName, false));
        commands.put("print_field_ascending_engine_power", new CommandWrapper(printFieldAscendingEnginePower, false));
        
        final CommandHelp help = new CommandHelp(getCommands());
        commands.put("help", new CommandWrapper(help, false)); 
    }

    public Map<String, Boolean> getCommandsWithInputRequirement() {
        Map<String, Boolean> inputRequirements = new HashMap<>();
        for (Map.Entry<String, CommandWrapper> entry : commands.entrySet()) {
            inputRequirements.put(entry.getKey(), entry.getValue().requiresInput());
        }
        return inputRequirements;
    }
    
    static Map<String, Command> getCommands() {
        Map<String, Command> commandMap = new HashMap<>();
        for (Map.Entry<String, CommandWrapper> entry : commands.entrySet()) {
            commandMap.put(entry.getKey(), entry.getValue().getCommand());
        }
        return commandMap;
    }
}