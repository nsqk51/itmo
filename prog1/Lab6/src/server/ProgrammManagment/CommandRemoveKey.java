package server.ProgrammManagment;

import server.entity.VehicleCollection;

/**
 * Команда для удаления элемента из коллекции по ключу.
 * <p>
 * Эта команда удаляет транспортное средство из коллекции по указанному ключу.
 * Если элемент с таким ключом существует, он будет удален. Если элемента нет,
 * будет выведено сообщение об ошибке.
 * </p>
 */
public class CommandRemoveKey extends Command {

    /**
     * Выполняет команду удаления элемента по ключу.
     * <p>
     * Метод пытается распарсить переданный аргумент как целое число, представляющее ключ.
     * Если элемент с таким ключом существует в коллекции, он будет удален.
     * Если ключ некорректен или элемент не найден, будет выведено соответствующее сообщение.
     * </p>
     *
     * @param args Аргументы команды, должен быть передан один аргумент — ключ элемента.
     */
    @Override
    public String execute(String[] args) {
        CommandHistory.addCommand("remove_key");

        if (args.length != 1) {
            return "Данная команда должна содержать 1 аргумент(ключ элемента).";
        } else {
            try {
                Long key = Long.parseLong(args[0]);

                if (VehicleCollection.getVehicle().containsKey(key)) {
                    VehicleCollection.getVehicle().remove(key);
                    return "Элемент с ключом " + key + " успешно удален.";
                } else {
                    return "Ошибка: Элемента с таким ключом не существует.";
                }
            } catch (NumberFormatException e) {
                return "Ошибка: Ключ должен быть целым числом.";
            }
        }
    }
}
