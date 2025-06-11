package server.entity;

import java.io.Serializable;
/**
 * Перечисление типов транспортных средств.
 * <p>
 * Определяет допустимые категории транспортных средств в системе.
 * </p>
 */
public enum VehicleType implements Serializable{
    /** Вертолёт */
    HELICOPTER,
    
    /** Дрон */
    DRONE,
    
    /** Лодка */
    BOAT,
    
    /** Велосипед */
    BICYCLE,
    
    /** Мотоцикл */
    MOTORCYCLE;
}