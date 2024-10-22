package dk.via.cars.data;

import dk.via.cars.business.persistence.DuplicateKeyException;
import dk.via.cars.business.persistence.NotFoundException;
import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Scope("singleton")
public class CarDAO implements Persistence {
	private final DatabaseHelper<Car> helper;

	public CarDAO(DatabaseHelper<Car> helper) {
		this.helper = helper;
	}

	public Car create(String licenseNo, String model, int year, Money price) throws PersistenceException {
        try {
            helper.executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.amount(), price.currency());
        	return new Car(licenseNo, model, year, price);
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key"))
                throw new DuplicateKeyException(e);
            else
                throw new PersistenceException(e);
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

	public Car read(String licenseNumber) throws PersistenceException {
        try {
            Car car = helper.mapSingle(this::createCar, "SELECT * FROM car where license_number = ?", licenseNumber);
            if (car == null) throw new NotFoundException();
            return car;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

	public Collection<Car> readAll() throws PersistenceException {
        try {
            return helper.map(this::createCar, "SELECT * FROM car");
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

	public void update(Car car) throws PersistenceException {
        try {
            int updated = helper.executeUpdate(
            "UPDATE car SET model=?, year=?, price_amount=?, price_currency=? WHERE license_number = ?",
                car.getModel(), car.getYear(), car.getPrice().amount(), car.getPrice().currency(), car.getLicenseNumber());
            if (updated == 0) throw new NotFoundException();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

	public void delete(String licenseNumber) throws PersistenceException {
        try {
            helper.executeUpdate("DELETE FROM car WHERE license_number = ?", licenseNumber);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
