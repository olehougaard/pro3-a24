package dk.via.cars.model;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null) throw new ValidationException("amount is required");
        if (currency == null || currency.isEmpty()) throw new ValidationException("currency is required");
    }
}
