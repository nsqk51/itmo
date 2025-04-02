package ProgrammManagment;

import entity.Vehicle;
import entity.VehicleCollection;

/**
 * Команда для замены элемента коллекции, если новое значение меньше текущего.
 * <p>
 * Эта команда позволяет заменить элемент в коллекции на новый, если его мощность двигателя меньше, чем у
 * уже существующего элемента с данным ключом. Для этого команда вызывает команду обновления элемента,
 * а затем проверяет, если новое значение меньше текущего, производит замену. Если новое значение не меньше,
 * замена отменяется.
 * </p>
 */
public class CommandReplaceIfLowe extends Command {

    private final CommandUpdate commandUpdate;

    /**
     * Конструктор класса.
     * <p>
     * Создает команду замены, передавая команду обновления для использования в процессе замены.
     * </p>
     *
     * @param commandUpdate Объект команды для обновления данных транспортного средства.
     */
    public CommandReplaceIfLowe(CommandUpdate commandUpdate) {
        this.commandUpdate = commandUpdate;
    }

    /**
     * Выполняет команду замены элемента коллекции, если новое значение меньше текущего.
     * <p>
     * Команда выполняет следующие действия:
     * <ol>
     *   <li>Запрашивает ключ элемента для замены.</li>
     *   <li>Проверяет наличие элемента с данным ключом в коллекции.</li>
     *   <li>Запрашивает данные для обновления транспортного средства.</li>
     *   <li>Сравнивает мощность двигателя текущего и обновленного элемента.</li>
     *   <li>Если новое значение меньше текущего, элемент заменяется; если нет — замена отменяется.</li>
     * </ol>
     * </p>
     *
     * @param args Аргументы команды: 1 аргумент (ключ элемента, который требуется заменить).
     */
    @Override
    public void execute(String[] args) {
        CommandHistory.addCommand("replace_if_lowe");

        if (args.length != 1) {
            System.out.println("Ошибка: Команда replace_if_lowe требует 1 аргумент (ключ).");
            return;
        }

        try {
            Long key = Long.parseLong(args[0]);

            if (!VehicleCollection.getVehicle().containsKey(key)) {
                System.out.println("Ошибка: Элемента с ключом '" + key + "' не существует.");
                return;
            }

            Vehicle existingVehicle = VehicleCollection.getVehicle().get(key);
            Long currentEnginePower = existingVehicle.getEnginePower();

            System.out.println("Введите данные нового транспортного средства для сравнения:");
            commandUpdate.execute(args);

            Vehicle updatedVehicle = VehicleCollection.getVehicle().get(key);

            if (updatedVehicle.getEnginePower() < currentEnginePower) {
                System.out.println("Замена успешно выполнена! Новое значение меньше старого.");
            } else {
                System.out.println("Отмена замены: новое значение (" + updatedVehicle.getEnginePower() +
                        ") не меньше текущего (" + currentEnginePower + ").");
                VehicleCollection.getVehicle().put(key, existingVehicle);
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть числом.");
        }
    }
}