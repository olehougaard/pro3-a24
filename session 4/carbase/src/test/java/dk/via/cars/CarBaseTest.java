package dk.via.cars;

import dk.via.CarbaseApplication;
import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.client.dry.Client;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
