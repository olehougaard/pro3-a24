package dk.via.cars.data;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;

import java.util.Collection;

public interface CarDAO {
	Car create(String licenseNo, String model, int year, Money price) throws PersistanceException;
	Collection<Car> readAll() throws PersistanceException;
	void update(Car car) throws PersistanceException;
	void delete(Car car) throws PersistanceException;
	Car read(String licenseNumber) throws PersistanceException;
}
