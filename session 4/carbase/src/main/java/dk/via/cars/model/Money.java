package dk.via.cars.model;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
}
