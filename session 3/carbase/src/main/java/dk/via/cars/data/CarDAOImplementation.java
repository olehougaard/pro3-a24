package dk.via.cars.data;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class CarDAOImplementation implements CarDAO {
	private final DatabaseHelper<Car> helper;

	public CarDAOImplementation(DatabaseHelper<Car> helper) {
		this.helper = helper;
	}

	public Car create(String licenseNo, String model, int year, Money price) throws PersistanceException {
        try {
            helper.executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.amount(), price.currency());
        	return new Car(licenseNo, model, year, price);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
	}
	
	private Car createCar(ResultSet rs) throws SQLException {
		String licenseNo = rs.getString("license_number");
		String model = rs.getString("model");
		int year = rs.getInt("year");
		BigDecimal priceAmount = rs.getBigDecimal("price_amount");
		String priceCurrency = rs.getString("price_currency");
		Money price = new Money(priceAmount, priceCurrency);
		return new Car(licenseNo, model, year, price);
	}

	public Car read(String licenseNumber) throws PersistanceException {
        try {
            return helper.mapSingle(this::createCar, "SELECT * FROM car where license_number = ?", licenseNumber);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

	public Collection<Car> readAll() throws PersistanceException {
        try {
            return helper.map(this::createCar, "SELECT * FROM car");
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

	public void update(Car car) throws PersistanceException {
        try {
            helper.executeUpdate("UPDATE car SET model=?, year=?, price_amount=?, price_currency=? WHERE license_number = ?",
                    car.getModel(), car.getYear(), car.getPrice().amount(), car.getPrice().currency(), car.getLicenseNumber());
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

	public void delete(Car car) throws PersistanceException {
        try {
            helper.executeUpdate("DELETE FROM car WHERE license_number = ?", car.getLicenseNumber());
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }
}
