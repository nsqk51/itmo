package entity;

/**
 * Класс, представляющий транспортное средство.
 * <p>
 * Каждое транспортное средство имеет уникальный идентификатор, название, координаты, дату создания,
 * мощность двигателя, расход топлива, пройденное расстояние и тип.
 * </p>
 */
public class Vehicle {
    private int id; // Значение поля должно быть больше 0, уникально и генерироваться автоматически
    private String name; // Поле не может быть null или пустым
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDateTime creationDate; // Поле не может быть null и генерируется автоматически
    private Long enginePower; // Поле может быть null, но если задано, должно быть больше 0
    private float fuelConsumption; // Значение должно быть больше 0
    private long distanceTravelled; // Значение должно быть больше 0
    private VehicleType type; // Поле может быть null

    /**
     * Конструктор для создания транспортного средства с заданным ID.
     *
     * @param id               Уникальный идентификатор (должен быть больше 0)
     * @param name             Название транспортного средства (не может быть null или пустым)
     * @param coordinates      Координаты (не могут быть null)
     * @param creationDate     Дата создания (не может быть null, должна генерироваться автоматически)
     * @param enginePower      Мощность двигателя (может быть null, но если задана, должна быть больше 0)
     * @param fuelConsumption  Расход топлива (должен быть больше 0)
     * @param distanceTravelled Пройденное расстояние (должно быть больше 0)
     * @param type             Тип транспортного средства (может быть null)
     * @throws IllegalArgumentException если данные не соответствуют ограничениям
     */
    public Vehicle(int id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate, Long enginePower, float fuelConsumption, long distanceTravelled, VehicleType type) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setEnginePower(enginePower);
        setFuelConsumption(fuelConsumption);
        setDistanceTravelled(distanceTravelled);
        setType(type);
    }

    /**
     * Конструктор для создания транспортного средства с автоматически сгенерированным ID.
     *
     * @param name             Название транспортного средства (не может быть null или пустым)
     * @param coordinates      Координаты (не могут быть null)
     * @param creationDate     Дата создания (не может быть null, должна генерироваться автоматически)
     * @param enginePower      Мощность двигателя (может быть null, но если задана, должна быть больше 0)
     * @param fuelConsumption  Расход топлива (должен быть больше 0)
     * @param distanceTravelled Пройденное расстояние (должно быть больше 0)
     * @param type             Тип транспортного средства (может быть null)
     * @throws IllegalArgumentException если данные не соответствуют ограничениям
     */
    public Vehicle(String name, Coordinates coordinates, java.time.LocalDateTime creationDate, Long enginePower, float fuelConsumption, long distanceTravelled, VehicleType type) {
        this.id = (int) VehicleCollection.generateId();
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setEnginePower(enginePower);
        setFuelConsumption(fuelConsumption);
        setDistanceTravelled(distanceTravelled);
        setType(type);
    }

    /**
     * Возвращает уникальный идентификатор транспортного средства.
     *
     * @return ID транспортного средства
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает название транспортного средства.
     *
     * @return Название
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты транспортного средства.
     *
     * @return Координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
    /**
     * Возвращает дату создания транспортного средства.
     *
     * @return Дата создания
     */
    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает мощность двигателя транспортного средства.
     *
     * @return Мощность двигателя (или null, если не задана)
     */
    public Long getEnginePower() {
        return enginePower;
    }

    /**
     * Возвращает расход топлива транспортного средства.
     *
     * @return Расход топлива
     */
    public float getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Возвращает пройденное расстояние транспортного средства.
     *
     * @return Пройденное расстояние
     */
    public long getDistanceTravelled() {
        return distanceTravelled;
    }

    /**
     * Возвращает тип транспортного средства.
     *
     * @return Тип (или null, если не задан)
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Устанавливает уникальный идентификатор транспортного средства.
     *
     * @param id Новый идентификатор (должен быть больше 0)
     * @throws IllegalArgumentException если ID меньше или равен 0
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Поле id должно быть больше 0");
        }
        this.id = id;
    }

    /**
     * Устанавливает название транспортного средства.
     *
     * @param name Новое название (не может быть null или пустым)
     * @throws IllegalArgumentException если имя пустое или null
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле name не может быть null или пустым");
        }
        this.name = name;
    }

    /**
     * Устанавливает координаты транспортного средства.
     *
     * @param coordinates Новые координаты (не могут быть null)
     * @throws IllegalArgumentException если координаты равны null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле coordinates не может быть null");
        }
        this.coordinates = coordinates;
    }

    /**
     * Устанавливает дату создания транспортного средства.
     *
     * @param creationDate Новая дата создания (не может быть null)
     * @throws IllegalArgumentException если дата null
     */
    public void setCreationDate(java.time.LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Поле creationDate не может быть null и должно генерироваться автоматически");
        }
        this.creationDate = creationDate;
    }

    /**
     * Устанавливает мощность двигателя транспортного средства.
     *
     * @param enginePower Новая мощность (должна быть больше 0, если не null)
     * @throws IllegalArgumentException если значение меньше или равно 0
     */
    public void setEnginePower(Long enginePower) {
        if (enginePower != null && enginePower <= 0) {
            throw new IllegalArgumentException("Поле enginePower должно быть больше 0, если не null");
        }
        this.enginePower = enginePower;
    }

    /**
     * Устанавливает расход топлива.
     *
     * @param fuelConsumption Новый расход (должен быть больше 0)
     * @throws IllegalArgumentException если значение меньше или равно 0
     */
    public void setFuelConsumption(float fuelConsumption) {
        if (fuelConsumption <= 0) {
            throw new IllegalArgumentException("Поле fuelConsumption должно быть больше 0");
        }
        this.fuelConsumption = fuelConsumption;
    }
    /**
     * Устанавливает пройденное расстояние.
     *
     * @param distanceTravelled Новое расстояние (должно быть больше 0)
     * @throws IllegalArgumentException если значение меньше или равно 0
     */
    public void setDistanceTravelled(long distanceTravelled) {
        if (distanceTravelled <= 0) {
            throw new IllegalArgumentException("Поле distanceTravelled должно быть больше 0");
        }
        this.distanceTravelled = distanceTravelled;
    }

    /**
     * Устанавливает тип транспортного средства.
     *
     * @param type Новый тип (может быть null)
     */
    public void setType(VehicleType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" + '\n' +
                "id=" + id + '\n' +
                "name=" + name + '\n' +
                "coordinates:" + '\n' +
                "   X = " + coordinates.getX() + "  Y = " + coordinates.getY() + '\n' +
                "creationDate=" + creationDate + '\n' +
                "enginePower=" + (enginePower != null ? enginePower : "null") + '\n' +
                "fuelConsumption=" + fuelConsumption + '\n' +
                "distanceTravelled=" + distanceTravelled + '\n' +
                "type=" + (type != null ? type : "null") + '\n' +
                '}';
    }
}