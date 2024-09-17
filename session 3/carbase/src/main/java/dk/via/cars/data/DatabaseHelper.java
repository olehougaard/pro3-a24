package dk.via.cars.data;

import org.postgresql.Driver;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper<T> {
	private final String jdbcURL;
	private final String username;
	private final String password;
	
	public DatabaseHelper(String jdbcURL, String username, String password) throws SQLException {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
		DriverManager.registerDriver(new Driver());
	}
	
	public DatabaseHelper(String jdbcURL) throws SQLException {
		this(jdbcURL, null, null);
	}
	
	protected Connection getConnection() throws SQLException {
		if (username == null) {
			return DriverManager.getConnection(jdbcURL);
		} else {
			return DriverManager.getConnection(jdbcURL, username, password);
		}
	}

	private PreparedStatement prepare(Connection connection, String sql, Object[] parameters) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql);
		for(int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}

	public int executeUpdate(String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			return stat.executeUpdate();
		}
	}
	
	public T mapSingle(DataMapper<T> mapper, String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			ResultSet rs = stat.executeQuery();
			if(rs.next()) {
				return mapper.create(rs);
			} else {
				return null;
			}
		}
	}

	public List<T> map(DataMapper<T> mapper, String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			ResultSet rs = stat.executeQuery();
			LinkedList<T> allCars = new LinkedList<>();
			while(rs.next()) {
				allCars.add(mapper.create(rs));
			}
			return allCars;
		}
	}
}
