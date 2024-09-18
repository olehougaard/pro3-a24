package dk.via.cars.business.persistence;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;

import java.util.Collection;

public interface Persistence {
	Car create(String licenseNo, String model, int year, Money price) throws PersistenceException;
	Collection<Car> readAll() throws PersistenceException;
	void update(Car car) throws PersistenceException;
	void delete(Car car) throws PersistenceException;
	Car read(String licenseNumber) throws PersistenceException;
}
