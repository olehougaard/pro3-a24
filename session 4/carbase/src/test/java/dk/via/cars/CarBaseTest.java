package dk.via.cars;

import dk.via.cars.client.dry.Client;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarBaseTest {
	private Client carBase;
	
	@BeforeEach
	public void setUp() {
		carBase = new Client("localhost", 9090);
	}

	@Test
	public void test() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBase.getCar("AV 41 213");
		car.setPrice(eur);
		carBase.updateCar(car);
		List<Car> allCars = carBase.getAllCars();
		assertEquals(1, allCars.size());
		assertEquals(eur, allCars.get(0).getPrice());
	}
}
