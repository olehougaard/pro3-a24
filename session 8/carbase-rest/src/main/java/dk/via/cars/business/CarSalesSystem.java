package dk.via.cars.business;

import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import dk.via.cars.model.ValidationException;

import java.util.List;

public interface CarSalesSystem {
    Car registerCar(String licenseNumber, String model, int year, Money price) throws PersistenceException, ValidationException;

    Car getCar(String licenseNumber) throws PersistenceException;

    List<Car> getAllCars() throws PersistenceException;

    void updateCar(Car car) throws PersistenceException, ValidationException;

    void removeCar(String licenseNumber) throws PersistenceException, ValidationException;
}
