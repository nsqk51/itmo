package server.Sup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import server.entity.Vehicle;
import server.entity.VehicleCollection;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для записи коллекции транспортных средств в JSON-файл.
 * <p>
 * Этот класс предоставляет метод для сериализации списка транспортных средств в формат JSON и записи этого JSON
 * в файл. Для записи используется библиотека Gson. Данные транспортных средств преобразуются в строковый формат,
 * включая специальное форматирование для {@link LocalDateTime}.
 */
public class ParseToJson {

    /**
     * Метод для записи коллекции транспортных средств в JSON-файл.
     * <p>
     * Сначала сортируется коллекция транспортных средств, затем каждый объект в списке преобразуется в строку JSON.
     * Весь список записывается в указанный файл с использованием библиотеки Gson. Форматирование JSON будет сделано
     * с отступами для удобства чтения.
     *
     * @param filePath Путь к файлу, в который будет записана коллекция.
     */
    public static void writeVehiclesToFile(String filePath) {
        List<Vehicle> sortedVehicles = VehicleCollection.getSortList();
        
        // Создаем объект Gson с настройкой на красивое форматирование и адаптером для LocalDateTime
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             OutputStreamWriter writer = new OutputStreamWriter(bufferedOutputStream)) {
            
            // Преобразуем коллекцию транспортных средств в JSON и записываем в файл
            gson.toJson(sortedVehicles, writer);
            System.out.println("Коллекция успешно записана в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}