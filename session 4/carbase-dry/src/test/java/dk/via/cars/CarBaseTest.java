package dk.via.cars;

import dk.via.CarbaseApplication;
import dk.via.cars.client.Client;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		classes = CarbaseApplication.class,
		webEnvironment = DEFINED_PORT,
		properties = "spring.main.allow-bean-definition-overriding=true")
@Import(TestContextConfiguration.class)
public class CarBaseTest {
	private Client carBaseClient;

	@BeforeEach
	public void setUp() {
		carBaseClient = new Client("localhost", 9090);
	}

	@Test
	public void update_changes_the_car_on_the_server() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBaseClient.getCar("AV 41 213");
		car.setPrice(eur);
		carBaseClient.updateCar(car);
		List<Car> allCars = carBaseClient.getAllCars();
		assertEquals(1, allCars.size());
		assertEquals(eur, allCars.get(0).getPrice());
	}

	@Test
	public void create_adds_the_car_on_the_server() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBaseClient.registerCar("AB 12 345", "Audi", 2019, eur);
		assertEquals("AB 12 345", car.getLicenseNumber());
		assertEquals(eur, car.getPrice());
		assertEquals("Audi", car.getModel());
		assertEquals(2019, car.getYear());
		List<Car> allCars = carBaseClient.getAllCars();
		assertEquals(2, allCars.size());
		carBaseClient.removeCar(car);
	}

	@Test
	public void duplicate_license_numbers_gives_an_exception() {
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		StatusRuntimeException e = assertThrows(StatusRuntimeException.class, () ->
			carBaseClient.registerCar("AV 41 213", "Audi", 2019, eur));
		assertTrue(e.getMessage().contains("Duplicate license plate"));
	}
}
