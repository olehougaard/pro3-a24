package dk.via.cars.grpc;

import dk.via.carbase.Money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class GrpcToModel {
    public static dk.via.cars.model.Money money(dk.via.carbase.Money price) {
        BigDecimal javaAmount = new BigDecimal(price.getAmount());
        return new dk.via.cars.model.Money(javaAmount, price.getCurrency());
    }

    public static dk.via.cars.model.Car car(dk.via.carbase.CarData carData) {
        String licenseNumber = carData.getLicenseNumber();
        String model = carData.getModel();
        int year = carData.getYear();
        Money price = carData.getPrice();
        return new dk.via.cars.model.Car(licenseNumber, model, year, money(price));
    }

    public static List<dk.via.cars.model.Car> cars(dk.via.carbase.CarsData cars) {
        return cars.getCarsList().stream().map(GrpcToModel::car).toList();
    }
}
