package dk.via.client;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CarsClient {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public CarsClient(String endpoint) {
        this.endpoint = endpoint + "/cars";
        restTemplate = new RestTemplate();
    }

    private String specificUrl(String licenseNumber) {
        return endpoint + "/" + licenseNumber;
    }

    public Car registerCar(String licenseNumber, String model, int year, Money price) {
        Car car = new Car(licenseNumber, model, year, price);
        return restTemplate.postForObject(endpoint, car, Car.class);
    }

    public Car getCar(String licenseNumber) {
      return restTemplate.getForObject(specificUrl(licenseNumber), Car.class);
    }

    public List<Car> getAllCars() {
        //noinspection DataFlowIssue
        return List.of(restTemplate.getForObject(endpoint, Car[].class));
    }

    public void updateCar(Car car) {
        restTemplate.put(specificUrl(car.getLicenseNumber()), car);
    }

    public void removeCar(String licenseNumber) {
        restTemplate.delete(specificUrl(licenseNumber));
    }
}
