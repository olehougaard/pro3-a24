package dk.via.cars;

import dk.via.cars.client.Client;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CarBaseTest {
	private Client carBase;
	
	@Before
	public void setUp() {
		carBase = new Client("localhost", 9090);
	}

	@Test
	public void test() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBase.getCar("AV 41 213");
		car.setPrice(eur);
		List<Car> allCars = carBase.getAllCars();
		assertEquals(1, allCars.size());
		//assertEquals(eur, allCars.get(0).getPrice());
	}
}
