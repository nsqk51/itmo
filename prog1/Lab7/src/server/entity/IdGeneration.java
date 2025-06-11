package server.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGeneration {

	
	private final Connection connection;

	public IdGeneration(Connection connection) {
        this.connection = connection;
        
    }

    /**
     * Метод для получения текущего значения из последовательности.
     * Требуется, чтобы сначала был вызван nextval.
     * 
     * @return текущее значение последовательности
     * @throws SQLException
     */
    public long getCurrentIdFromSequence() throws SQLException {
    	String sql = "SELECT currval('vehicle_id_seq')";  // Запрос для получения максимального id из таблицы movie
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);  // Возвращаем максимальный id
            } else {
                throw new SQLException("Не удалось получить последний id фильма из таблицы vehicle");
            }
        }
    }
	
}