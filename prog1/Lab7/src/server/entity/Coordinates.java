package server.entity;

import java.util.Objects;
import java.io.Serializable;

/**
 * Класс, представляющий координаты.
 * <p>
 * Поля x и y имеют ограничения по минимальным значениям.
 * </p>
 */
public class Coordinates implements Serializable{
    private Long x; // Значение поля должно быть больше -978, не может быть null
    private long y; // Значение поля должно быть больше -45

    /**
     * Конструктор для создания координат.
     *
     * @param x Координата X (должна быть больше -978 и не может быть null)
     * @param y Координата Y (должна быть больше -45)
     * @throws IllegalArgumentException если значения выходят за допустимые границы
     */
    public Coordinates(Long x, long y) {
        setX(x);
        setY(y);
    }

    /**
     * Возвращает значение координаты X.
     *
     * @return Значение X
     */
    public Long getX() {
        return x;
    }

    /**
     * Возвращает значение координаты Y.
     *
     * @return Значение Y
     */
    public long getY() {
        return y;
    }

    /**
     * Устанавливает значение координаты X.
     *
     * @param x Новое значение X (должно быть больше -978 и не может быть null)
     * @throws IllegalArgumentException если X равен null или меньше/равен -978
     */
    public void setX(Long x) {
        if (x == null) {
            throw new IllegalArgumentException("Поле x не может быть null");
        }
        if (x <= -978) {
            throw new IllegalArgumentException("Поле x должно быть больше -978");
        }
        this.x = x;
    }

    /**
     * Устанавливает значение координаты Y.
     *
     * @param y Новое значение Y (должно быть больше -45)
     * @throws IllegalArgumentException если Y меньше/равен -45
     */
    public void setY(long y) {
        if (y <= -45) {
            throw new IllegalArgumentException("Поле y должно быть больше -45");
        }
        this.y = y;
    }

    /**
     * Проверяет равенство объектов по значениям X и Y.
     *
     * @param o Объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y && x.equals(that.x);
    }

    /**
     * Возвращает хеш-код объекта на основе координат X и Y.
     *
     * @return Хеш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return Строка в формате "Coordinates{x=X, y=Y}"
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
