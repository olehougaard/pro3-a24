package dk.via.cars;

import dk.via.cars.business.persistence.DuplicateKeyException;
import dk.via.cars.business.persistence.NotFoundException;
import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

class FakePersistence implements Persistence {
    private final HashMap<String, Car> cars = new HashMap<>();

    public FakePersistence(Car... cars) {
        Arrays.stream(cars).forEach(car -> this.cars.put(car.getLicenseNumber(), car));
    }

    @Override
    public Car create(String licenseNumber, String model, int year, Money price) throws PersistenceException {
        if (cars.containsKey(licenseNumber)) {
            throw new DuplicateKeyException();
        }
        cars.put(licenseNumber, new Car(licenseNumber, model, year, price));
        return cars.get(licenseNumber);
    }

    @Override
    public Collection<Car> readAll() {
        return cars.values();
    }

    @Override
    public void update(Car car) throws PersistenceException {
        if (!cars.containsKey(car.getLicenseNumber())) {
            throw new NotFoundException();
        }
        cars.put(car.getLicenseNumber(), car);
    }

    @Override
    public void delete(Car car) {
        cars.remove(car.getLicenseNumber());
    }

    @Override
    public Car read(String licenseNumber) {
        return cars.get(licenseNumber);
    }
}
