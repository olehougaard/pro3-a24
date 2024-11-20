package dk.via.cars;

import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import dk.via.client.CarsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarBaseTest {
	private CarsClient carBaseClient;

	@BeforeEach
	public void setUp() {
		carBaseClient = new CarsClient("localhost", 8080, "admin", "password");
	}

	@Test
	public void test() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBaseClient.getCar("AV 41 213");
		car.setPrice(eur);
		carBaseClient.updateCar(car);
		List<Car> allCars = carBaseClient.getAllCars();
		assertEquals(1, allCars.size());
		assertEquals(eur, allCars.get(0).getPrice());
	}
}
