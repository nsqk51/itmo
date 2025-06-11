package server.database;

import server.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class DatabaseVehicleManager {
	private final Connection connection;

	public DatabaseVehicleManager(Connection connection) {
		this.connection = connection;
	}

	public void initializeDatabase() throws SQLException {
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("""
					    CREATE TABLE IF NOT EXISTS coordinates (
					        id SERIAL PRIMARY KEY,
					        x DOUBLE PRECISION NOT NULL,
					        y BIGINT NOT NULL
					    );
					""");

			stmt.execute("""
					    CREATE TABLE IF NOT EXISTS vehicle (
					        id SERIAL PRIMARY KEY,
					        name TEXT NOT NULL,
					        coordinates_id INTEGER REFERENCES coordinates(id) ON DELETE CASCADE,
					        creation_date TIMESTAMP NOT NULL,
					        engine_power BIGINT,
					        fuel_consumption REAL NOT NULL,
					        distance_travelled BIGINT NOT NULL,
					        type TEXT,
					        created_by TEXT NOT NULL
					    );
					""");
		}
	}

	public void addVehicleToDatabase(Vehicle vehicle) throws SQLException {
		String insertCoordinatesSQL = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
		long coordinatesId;
		try (PreparedStatement ps = connection.prepareStatement(insertCoordinatesSQL)) {
			ps.setDouble(1, vehicle.getCoordinates().getX());
			ps.setLong(2, vehicle.getCoordinates().getY());
			ResultSet rs = ps.executeQuery();
			rs.next();
			coordinatesId = rs.getLong(1);
		}

		String insertVehicleSQL = """
				    INSERT INTO vehicle (name, coordinates_id, creation_date, engine_power, fuel_consumption, distance_travelled, type, created_by)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";
		try (PreparedStatement ps = connection.prepareStatement(insertVehicleSQL)) {
			ps.setString(1, vehicle.getName());
			ps.setLong(2, coordinatesId);
			ps.setTimestamp(3, Timestamp.valueOf(vehicle.getCreationDate()));
			if (vehicle.getEnginePower() != null)
				ps.setLong(4, vehicle.getEnginePower());
			else
				ps.setNull(4, Types.BIGINT);
			ps.setFloat(5, vehicle.getFuelConsumption());
			ps.setLong(6, vehicle.getDistanceTravelled());
			ps.setString(7, vehicle.getType() != null ? vehicle.getType().name() : null);
			ps.setString(8, vehicle.getCreatedBy());
			ps.executeUpdate();
		}
	}

	public HashMap<Long, Vehicle> loadAllVehicles() throws SQLException {
		HashMap<Long, Vehicle> vehicleTable = new HashMap<>();
		String query = """
				    SELECT v.id, v.name, v.creation_date, v.engine_power, v.fuel_consumption, v.distance_travelled, v.type,
				           v.created_by,
				           c.x AS coord_x, c.y AS coord_y
				    FROM vehicle v
				    JOIN coordinates c ON v.coordinates_id = c.id
				""";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Coordinates coords = new Coordinates(rs.getLong("coord_x"), rs.getLong("coord_y"));
				LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();

				Vehicle vehicle = new Vehicle(rs.getInt("id"), rs.getString("name"), coords, creationDate,
						rs.getObject("engine_power", Long.class), rs.getFloat("fuel_consumption"),
						rs.getLong("distance_travelled"),
						rs.getString("type") != null ? VehicleType.valueOf(rs.getString("type")) : null,
						rs.getString("created_by"));

				vehicleTable.put((long) vehicle.getId(), vehicle);
			}
		}

		return vehicleTable;
	}

	public void deleteVehicle(int vehicleId) throws SQLException {
		connection.setAutoCommit(false);

		try (PreparedStatement getCoordsStmt = connection
				.prepareStatement("SELECT coordinates_id FROM vehicle WHERE id = ?");
				PreparedStatement deleteVehicleStmt = connection.prepareStatement("DELETE FROM vehicle WHERE id = ?");
				PreparedStatement deleteCoordsStmt = connection
						.prepareStatement("DELETE FROM coordinates WHERE id = ?")) {
			Long coordinatesId = null;

			getCoordsStmt.setInt(1, vehicleId);
			try (ResultSet rs = getCoordsStmt.executeQuery()) {
				if (rs.next()) {
					coordinatesId = rs.getLong("coordinates_id");
				}
			}

			deleteVehicleStmt.setInt(1, vehicleId);
			deleteVehicleStmt.executeUpdate();

			if (coordinatesId != null) {
				deleteCoordsStmt.setLong(1, coordinatesId);
				deleteCoordsStmt.executeUpdate();
			}

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public void updateVehicle(int id, Vehicle vehicle) throws SQLException {
		String updateVehicleSQL = """
				    UPDATE vehicle
				    SET name = ?, engine_power = ?, fuel_consumption = ?, distance_travelled = ?, type = ?
				    WHERE id = ?
				""";
		String updateCoordinatesSQL = """
				    UPDATE coordinates
				    SET x = ?, y = ?
				    WHERE id = (SELECT coordinates_id FROM vehicle WHERE id = ?)
				""";

		try (PreparedStatement vehicleStmt = connection.prepareStatement(updateVehicleSQL);
				PreparedStatement coordStmt = connection.prepareStatement(updateCoordinatesSQL)) {
			vehicleStmt.setString(1, vehicle.getName());
			if (vehicle.getEnginePower() != null)
				vehicleStmt.setLong(2, vehicle.getEnginePower());
			else
				vehicleStmt.setNull(2, Types.BIGINT);
			vehicleStmt.setFloat(3, vehicle.getFuelConsumption());
			vehicleStmt.setLong(4, vehicle.getDistanceTravelled());
			vehicleStmt.setString(5, vehicle.getType() != null ? vehicle.getType().name() : null);
			vehicleStmt.setInt(6, id);
			vehicleStmt.executeUpdate();

			coordStmt.setDouble(1, vehicle.getCoordinates().getX());
			coordStmt.setLong(2, vehicle.getCoordinates().getY());
			coordStmt.setInt(3, id);
			coordStmt.executeUpdate();
		}
	}

	public Map<Long, String> getIdOwners() throws SQLException {
		Map<Long, String> idOwners = new HashMap<>();
		String sql = "SELECT id, created_by FROM vehicle";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				long id = rs.getLong("id");
				String owner = rs.getString("created_by");
				idOwners.put(id, owner);
			}
		}
		return idOwners;
	}

}
