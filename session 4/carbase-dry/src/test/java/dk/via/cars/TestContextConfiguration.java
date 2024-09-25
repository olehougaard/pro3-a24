package dk.via.cars;

import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@TestConfiguration
public class TestContextConfiguration {
    @Bean
    public Persistence persistence() {
        Car car = new Car("AV 41 213", "Ford", 2014, new Money(new BigDecimal("4999.99"), "DKK"));
        return new FakePersistence(car);
    }

}
