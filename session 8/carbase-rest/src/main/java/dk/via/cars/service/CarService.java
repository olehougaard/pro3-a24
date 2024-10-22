package dk.via.cars.service;

import dk.via.cars.business.CarSalesSystem;
import dk.via.cars.business.persistence.DuplicateKeyException;
import dk.via.cars.business.persistence.NotFoundException;
import dk.via.cars.business.persistence.PersistenceException;
import dk.via.cars.model.Car;
import dk.via.cars.model.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarService {
    private final CarSalesSystem sales;

    public CarService(CarSalesSystem sales) {
        this.sales = sales;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNoSuchElementFoundException (DuplicateKeyException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNoSuchElementFoundException (NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNoSuchElementFoundException (ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @PostMapping
    public Car registerCar(@RequestBody Car car) throws PersistenceException {
        return sales.registerCar(car.getLicenseNumber(), car.getModel(), car. getYear(), car.getPrice());
    }

    @GetMapping("/{licenseNumber}")
    public Car getCar(@PathVariable("licenseNumber") String licenseNumber) throws PersistenceException {
        return sales.getCar(licenseNumber);
    }

    @GetMapping
    public List<Car> getAllCars() throws PersistenceException {
        return sales.getAllCars();
    }

    @PutMapping("/{licenseNumber}")
    public void updateCar(@PathVariable("licenseNumber") String licenseNumber, @RequestBody Car car) throws PersistenceException {
        if (!licenseNumber.equals(car.getLicenseNumber())) {
            throw new ValidationException("License number does not match");
        }
        sales.updateCar(car);
    }

    @DeleteMapping("/{licenseNumber}")
    public void deleteCar(@PathVariable("licenseNumber") String licenseNumber) throws PersistenceException {
        sales.removeCar(licenseNumber);
    }
}
