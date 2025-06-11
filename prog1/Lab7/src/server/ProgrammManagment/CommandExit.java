package server.ProgrammManagment;

public class CommandExit extends Command {

	@Override 
    public Boolean checkUser(String login, String password){
		return null;
    }
	
    @Override
    public String execute(String[] args, String login, String password) {
        if (args.length != 0) {
            return "Команда exit не должна содержать аргументов.";
        } else {
            CommandSave save = new CommandSave();
			save.execute(args, login, password);

			// Сообщаем клиенту, что он может завершить работу
			return "Клиент завершает работу. Коллекция ранспорта сохранена.";
        }
    }
}
