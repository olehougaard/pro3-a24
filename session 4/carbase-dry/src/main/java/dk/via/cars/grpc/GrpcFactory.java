package dk.via.cars.grpc;

import dk.via.carbase.CarData;
import dk.via.carbase.CarId;
import dk.via.carbase.EmptyMessage;
import dk.via.cars.model.Money;

public class GrpcFactory {
    public static CarData createCar(String licenseNumber, String model, int year, Money price) {
        return CarData.newBuilder()
                .setLicenseNumber(licenseNumber)
                .setModel(model)
                .setYear(year)
                .setPrice(ModelToGrpc.money(price)).build();
    }

    public static CarId createCarId(String licenseNumber) {
        return CarId.newBuilder().setLicenseNumber(licenseNumber).build();
    }

    public static EmptyMessage createEmptyMessage() {
        return EmptyMessage.newBuilder().build();
    }
}
