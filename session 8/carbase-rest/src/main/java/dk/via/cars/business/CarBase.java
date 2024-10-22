package dk.via.cars.business;

import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Money;
import dk.via.cars.model.Car;
import dk.via.cars.model.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class CarBase implements CarSalesSystem {
	private final Map<String, Car> carsCache = new HashMap<>();
	private final Persistence persistence;

	public CarBase(Persistence persistence) {
		this.persistence = persistence;
	}

	private static final Pattern licensePattern = Pattern.compile("^[A-Z]{2} \\d{2} \\d{3}$");

	@Override
	public Car registerCar(String licenseNumber, String model, int year, Money price) throws PersistenceException, ValidationException {
		if (price.amount().compareTo(BigDecimal.ZERO) < 0) throw new ValidationException("price must be greater than zero");
		if (!licensePattern.matcher(licenseNumber).matches()) throw new ValidationException("license number is invalid");

		Car car = persistence.create(licenseNumber, model, year, price);
		carsCache.put(licenseNumber, car);
		return car;
	}

	@Override
	public Car getCar(String licenseNumber) throws PersistenceException, ValidationException {
		if (licenseNumber == null || licenseNumber.isEmpty()) throw new ValidationException("licenseNumber is required");

		if (!carsCache.containsKey(licenseNumber)) {
			carsCache.put(licenseNumber, persistence.read(licenseNumber));
		}
		return carsCache.get(licenseNumber);
	}

	@Override
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

	@Override
	public void updateCar(Car car) throws PersistenceException, ValidationException {
		if (car == null) throw new ValidationException("car is required");
		carsCache.put(car.getLicenseNumber(), car);
		persistence.update(car);
	}

	@Override
	public void removeCar(String licenseNumber) throws PersistenceException, ValidationException {
		carsCache.remove(licenseNumber);
		persistence.delete(licenseNumber);
	}
}
