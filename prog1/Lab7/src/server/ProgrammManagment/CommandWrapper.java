package server.ProgrammManagment;

public class CommandWrapper {
	private final Command command;
    private final boolean requiresInput;
 
    public CommandWrapper(Command command, boolean requiresInput) {
    	this.command = command;
    	this.requiresInput = requiresInput;
    }

    public Command getCommand() {
    	return command;
    }

    public boolean requiresInput() {
    	return requiresInput;
    }
}
