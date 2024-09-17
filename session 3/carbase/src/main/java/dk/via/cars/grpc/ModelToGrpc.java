package dk.via.cars.grpc;

import java.util.List;

public class ModelToGrpc {
    public static dk.via.carbase.Money money(dk.via.cars.model.Money money) {
        dk.via.carbase.Money.Builder moneyBuilder = dk.via.carbase.Money.newBuilder()
                .setAmount(money.amount().toString())
                .setCurrency(money.currency());
        return moneyBuilder.build();
    }

    public static dk.via.carbase.CarData car(dk.via.cars.model.Car car) {
        dk.via.carbase.CarData.Builder carBuilder = dk.via.carbase.CarData.newBuilder()
                .setLicenseNumber(car.getLicenseNumber())
                .setModel(car.getModel())
                .setYear(car.getYear())
                .setPrice(money(car.getPrice()));
        return carBuilder.build();
    }

    public static dk.via.carbase.CarsData cars(List<dk.via.cars.model.Car> cars) {
        List<dk.via.carbase.CarData> carDataList = cars.stream().map(ModelToGrpc::car).toList();
        dk.via.carbase.CarsData.Builder carsBuilder = dk.via.carbase.CarsData.newBuilder()
                .addAllCars(carDataList);
        return carsBuilder.build();
    }
}
