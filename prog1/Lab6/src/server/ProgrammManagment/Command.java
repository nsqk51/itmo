package server.ProgrammManagment;

/**
 * Абстрактный класс, представляющий команду.
 * <p>
 * Все конкретные команды должны наследоваться от этого класса и реализовывать метод {@code execute}.
 * </p>
 */
public abstract class Command {

    /**
     * Выполняет команду с заданными аргументами.
     *
     * @param args массив строковых аргументов команды
     */
    public abstract String execute(String[] args);
}