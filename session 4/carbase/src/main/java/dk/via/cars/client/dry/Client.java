package dk.via.cars.client.dry;

import dk.via.carbase.CarData;
import dk.via.carbase.CarId;
import dk.via.carbase.CarsData;
import dk.via.cars.grpc.GrpcFactory;
import dk.via.cars.grpc.GrpcToModel;
import dk.via.cars.grpc.ModelToGrpc;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;

import java.util.List;

public class Client {
    private final GrpcAdapter adapter;

    public Client(String host, int port) {
        adapter = new GrpcAdapter(host, port);
    }

    public Car registerCar(String licenseNumber, String model, int year, Money price) {
        return adapter.apply(stub -> {
            CarData data = GrpcFactory.createCar(licenseNumber, model, year, price);
            CarData carData = stub.registerCar(data);
            return GrpcToModel.car(carData);
        });
    }

    public Car getCar(String licenseNumber) {
        return adapter.apply(stub -> {
            CarId carId = GrpcFactory.createCarId(licenseNumber);
            CarData carData = stub.getCar(carId);
            return GrpcToModel.car(carData);
        });
    }

    public List<Car> getAllCars() {
        return adapter.apply(stub -> {
            CarsData carsData = stub.getAllCars(GrpcFactory.createEmptyMessage());
            return GrpcToModel.cars(carsData);
        });
    }

    public void updateCar(Car car) {
        //noinspection ResultOfMethodCallIgnored
        adapter.execute(stub -> stub.updateCar(ModelToGrpc.car(car)));
    }

    public void removeCar(Car car) {
        //noinspection ResultOfMethodCallIgnored
        adapter.execute(stub -> stub.removeCar(ModelToGrpc.car(car)));
    }
}
