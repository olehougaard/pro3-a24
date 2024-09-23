package dk.via.cars.business;

import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Money;
import dk.via.cars.model.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Component
public class CarBase {
	private final Map<String, Car> carsCache = new HashMap<>();
	private final Persistence persistence;

	public CarBase(Persistence persistence) {
		this.persistence = persistence;
	}
	
	public Car registerCar(String licenseNumber, String model, int year, Money price) throws PersistenceException, ValidationException {
		if (licenseNumber == null || licenseNumber.isEmpty()) throw new ValidationException("licenseNumber is required");
		if (model == null || model.isEmpty()) throw new ValidationException("model is required");
		if (price == null) throw new ValidationException("price is required");
		if (price.amount().compareTo(BigDecimal.ZERO) < 0) throw new ValidationException("price must be greater than zero");

		Car car = persistence.create(licenseNumber, model, year, price);
		carsCache.put(licenseNumber, car);
		return car;
	}
	

	public Car getCar(String licenseNumber) throws PersistenceException, ValidationException {
		if (licenseNumber == null || licenseNumber.isEmpty()) throw new ValidationException("licenseNumber is required");

		if (!carsCache.containsKey(licenseNumber)) {
			carsCache.put(licenseNumber, persistence.read(licenseNumber));
		}
		return carsCache.get(licenseNumber);
	}

	public List<Car> getAllCars() throws PersistenceException {
		Collection<Car> allCars = persistence.readAll();
		LinkedList<Car> list = new LinkedList<>();
		for(Car car: allCars) {
			if (!carsCache.containsKey(car.getLicenseNumber())) {
				carsCache.put(car.getLicenseNumber(), car);
			}
			list.add(carsCache.get(car.getLicenseNumber()));
		}
		return list;
	}

	public void updateCar(Car car) throws PersistenceException, ValidationException {
		if (car == null) throw new ValidationException("car is required");

		carsCache.put(car.getLicenseNumber(), car);
		persistence.update(car);
	}

	public void removeCar(Car car) throws PersistenceException, ValidationException {
		if (car == null) throw new ValidationException("car is required");

		carsCache.remove(car.getLicenseNumber());
		persistence.delete(car);
	}
}
