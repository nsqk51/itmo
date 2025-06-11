package server.Sup;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер для сериализации и десериализации объектов {@link LocalDateTime} в формат JSON
 * и обратно с использованием библиотеки Gson.
 * <p>
 * Этот адаптер использует формат ISO_DATE_TIME для преобразования {@link LocalDateTime}.
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    // Форматтер для работы с датой и временем в формате ISO
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Сериализует объект {@link LocalDateTime} в JSON строку.
     * 
     * @param src объект {@link LocalDateTime}, который нужно сериализовать.
     * @param typeOfSrc тип объекта {@link LocalDateTime}.
     * @param context контекст сериализации.
     * @return {@link JsonElement}, представляющий объект {@link LocalDateTime} в формате JSON.
     */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));  // Преобразуем дату в строку в формате ISO
    }

    /**
     * Десериализует JSON строку в объект {@link LocalDateTime}.
     * 
     * @param json JSON строка, представляющая {@link LocalDateTime}.
     * @param typeOfT тип объекта {@link LocalDateTime}.
     * @param context контекст десериализации.
     * @return объект {@link LocalDateTime}, полученный из JSON строки.
     * @throws JsonParseException если формат строки не соответствует ожидаемому формату.
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);  // Преобразуем строку в LocalDateTime
    }
}
