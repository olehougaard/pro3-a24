package dk.via.cars.business;

import dk.via.cars.data.CarDAO;
import dk.via.cars.data.PersistanceException;
import dk.via.cars.model.Money;
import dk.via.cars.model.Car;

import java.util.*;

public class CarBase {
	private Map<String, Car> carsCache = new HashMap<>();
	private CarDAO dao;

	public CarBase(CarDAO dao) {
		this.dao = dao;
	}
	
	public Car registerCar(String licenseNumber, String model, int year, Money price) throws PersistanceException {
		Car car = dao.create(licenseNumber, model, year, price);
		carsCache.put(licenseNumber, car);
		return car;
	}
	

	public Car getCar(String licenseNumber) throws PersistanceException {
		if (!carsCache.containsKey(licenseNumber)) {
			carsCache.put(licenseNumber, dao.read(licenseNumber));
		}
		return carsCache.get(licenseNumber);
	}

	public List<Car> getAllCars() throws PersistanceException {
		Collection<Car> allCars = dao.readAll();
		LinkedList<Car> list = new LinkedList<Car>();
		for(Car car: allCars) {
			if (!carsCache.containsKey(car.getLicenseNumber())) {
				carsCache.put(car.getLicenseNumber(), car);
			}
			list.add(carsCache.get(car.getLicenseNumber()));
		}
		return list;
	}

	public void removeCar(Car car) throws PersistanceException {
		carsCache.remove(car.getLicenseNumber());
		dao.delete(car);
	}
}
