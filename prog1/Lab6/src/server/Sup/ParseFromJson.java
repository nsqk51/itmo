package server.Sup;

import com.google.gson.*;

import server.entity.Coordinates;
import server.entity.Vehicle;
import server.entity.VehicleType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для парсинга данных из JSON-файла и преобразования их в объекты {@link Vehicle}.
 * <p>
 * Этот класс читает JSON-файл и преобразует данные о транспортных средствах в список объектов {@link Vehicle}.
 * Если данные о транспортном средстве некорректны, они выводятся в ошибку и не добавляются в список.
 */
public class ParseFromJson {
    private BufferedReader reader;

    /**
     * Конструктор класса, который инициализирует объект для чтения данных из файла.
     *
     * @param filePath Путь к файлу JSON.
     * @throws IOException если файл не найден или возникает ошибка при чтении.
     */
    public ParseFromJson(String filePath) throws IOException {
        try {
            InputStream inputStream = new FileInputStream(filePath);
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (FileNotFoundException e) {
            throw new IOException("Ошибка: Файл " + filePath + " не найден.", e);
        }
    }

    /**
     * Метод для парсинга транспортных средств из JSON и их преобразования в список объектов {@link Vehicle}.
     * <p>
     * Каждый транспортный объект парсится из JSON, и если встречается ошибка (например, неверный формат или значение),
     * объект пропускается, а ошибка выводится в консоль.
     *
     * @return Список объектов {@link Vehicle}, считанных из JSON.
     * @throws IOException если возникает ошибка при чтении файла или обработке JSON.
     */
    public List<Vehicle> parseVehicles() throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        StringBuilder jsonContent = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JsonArray jsonArray = fromJson(jsonContent.toString(), JsonArray.class);

            for (JsonElement element : jsonArray) {
                try {
                    JsonObject obj = element.getAsJsonObject();

                    // Считываем данные из JSON
                    int id = obj.get("id").getAsInt();
                    String name = obj.get("name").getAsString();

                    JsonObject coordObj = obj.getAsJsonObject("coordinates");
                    Long x = coordObj.get("x").getAsLong();
                    long y = coordObj.get("y").getAsLong();

                    LocalDateTime creationDate = LocalDateTime.parse(obj.get("creationDate").getAsString());

                    Long enginePower = obj.has("enginePower") && !obj.get("enginePower").isJsonNull()
                            ? obj.get("enginePower").getAsLong()
                            : null;

                    float fuelConsumption = obj.get("fuelConsumption").getAsFloat();
                    long distanceTravelled = obj.get("distanceTravelled").getAsLong();

                    VehicleType type = obj.has("type") && !obj.get("type").isJsonNull()
                            ? VehicleType.valueOf(obj.get("type").getAsString())
                            : null;

                    Coordinates coordinates = new Coordinates(x, y);
                    Vehicle vehicle = new Vehicle(id, name, coordinates, creationDate, enginePower, fuelConsumption, distanceTravelled, type);
                    vehicles.add(vehicle);                } catch (JsonSyntaxException e) {
                        System.err.println("Объект удалён из коллекции: Ошибка парсинга JSON для транспортного средства с id: " + getIdFromJson(element) + " — Неверный синтаксис JSON: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Объект удалён из коллекции: Ошибка для транспортного средства с id: " + getIdFromJson(element) + " — Некорректное число: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Объект удалён из коллекции: Ошибка обработки JSON для транспортного средства с id: " + getIdFromJson(element) + " — " + e.getMessage());
                    }
                }
            } catch (JsonSyntaxException e) {
                throw new IOException("Ошибка: Некорректный JSON-файл. Проверьте формат.", e);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            return vehicles;
        }

        /**
         * Метод для преобразования строки JSON в объект указанного типа.
         *
         * @param jsonString строка JSON, которую необходимо преобразовать.
         * @param classOfT тип, в который нужно преобразовать JSON.
         * @param <T> тип возвращаемого объекта.
         * @return объект указанного типа, представленный в JSON.
         */
        private static <T> T fromJson(String jsonString, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, classOfT);
        }

        /**
         * Вспомогательный метод для получения ID транспортного средства из JSON.
         *
         * @param element элемент JSON, содержащий данные транспортного средства.
         * @return ID транспортного средства или -1, если ID не найден.
         */
        private int getIdFromJson(JsonElement element) {
            JsonObject obj = element.getAsJsonObject();
            return obj.has("id") ? obj.get("id").getAsInt() : -1;  // Возвращаем -1, если ID не найден
        }
    }