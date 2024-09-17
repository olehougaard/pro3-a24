package dk.via.cars.grpc;

import dk.via.carbase.Money;

import java.math.BigDecimal;
import java.math.BigInteger;

public class GrpcToModel {
    public static dk.via.cars.model.Money money(dk.via.carbase.Money price) {
        BigDecimal javaAmount = new BigDecimal(new BigInteger(price.getAmount()));
        return new dk.via.cars.model.Money(javaAmount, price.getCurrency());
    }

    public static dk.via.cars.model.Car car(dk.via.carbase.CarData carData) {
        String licenseNumber = carData.getLicenseNumber();
        String model = carData.getModel();
        int year = carData.getYear();
        Money price = carData.getPrice();
        return new dk.via.cars.model.Car(licenseNumber, model, year, money(price));
    }
}
