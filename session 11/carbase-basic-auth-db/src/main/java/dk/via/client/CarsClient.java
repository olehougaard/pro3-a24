package dk.via.client;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class CarsClient {
    private final WebClient webClient;

    public CarsClient(String host, int port, String username, String password) {
        String endpoint = String.format("http://%s:%d/cars", host, port);
        webClient = WebClient.builder()
                .baseUrl(endpoint)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    public Car registerCar(String licenseNumber, String model, int year, Money price) {
        Car car = new Car(licenseNumber, model, year, price);
        return webClient.post().body(Mono.just(car), Car.class).retrieve().bodyToMono(Car.class).block();
    }

    public Car getCar(String licenseNumber) {
      return webClient.get().uri("/" + licenseNumber).retrieve().bodyToMono(Car.class).block();
    }

    public List<Car> getAllCars() {
        //noinspection DataFlowIssue
        return List.of(webClient.get().retrieve().bodyToMono(Car[].class).block());
    }

    public void updateCar(Car car) {
        webClient.put().uri("/" + car.getLicenseNumber()).body(Mono.just(car), Car.class).retrieve().bodyToMono(Void.class).block();
    }

    public void removeCar(String licenseNumber) {
        webClient.put().uri("/" + licenseNumber).retrieve().bodyToMono(Void.class).block();
    }
}
