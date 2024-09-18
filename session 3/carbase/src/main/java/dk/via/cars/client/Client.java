package dk.via.cars.client;

import dk.via.carbase.CarData;
import dk.via.carbase.CarId;
import dk.via.carbase.CarServiceGrpc;
import dk.via.carbase.CarsData;
import dk.via.cars.grpc.GrpcFactory;
import dk.via.cars.grpc.GrpcToModel;
import dk.via.cars.grpc.ModelToGrpc;
import dk.via.cars.model.Car;
import dk.via.cars.model.Money;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private ManagedChannel channel() {
        return ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();
    }

    public Car registerCar(String licenseNumber, String model, int year, Money price) {
        ManagedChannel channel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(channel);
            CarData data = GrpcFactory.createCar(licenseNumber, model, year, price);
            CarData carData = stub.registerCar(data);
            return GrpcToModel.car(carData);
        } finally {
            channel.shutdown();
        }
    }

    public Car getCar(String licenseNumber) {
        ManagedChannel channel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(channel);
            CarId carId = GrpcFactory.createCarId(licenseNumber);
            CarData carData = stub.getCar(carId);
            return GrpcToModel.car(carData);
        } finally {
            channel.shutdown();
        }
    }

    public List<Car> getAllCars() {
        ManagedChannel channel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(channel);
            CarsData carsData = stub.getAllCars(GrpcFactory.createEmptyMessage());
            return GrpcToModel.cars(carsData);
        } finally {
            channel.shutdown();
        }
    }

    public void removeCar(Car car) {
        ManagedChannel channel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(channel);
            //noinspection ResultOfMethodCallIgnored
            stub.removeCar(ModelToGrpc.car(car));
        } finally {
            channel.shutdown();
        }
    }
}
