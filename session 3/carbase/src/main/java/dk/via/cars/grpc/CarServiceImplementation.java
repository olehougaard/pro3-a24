package dk.via.cars.grpc;

import dk.via.carbase.*;
import dk.via.cars.business.CarBase;
import dk.via.cars.data.PersistanceException;
import dk.via.cars.model.Car;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class CarServiceImplementation extends CarServiceGrpc.CarServiceImplBase {
    private final CarBase carBase;

    public CarServiceImplementation(CarBase carBase) {
        this.carBase = carBase;
    }

    @Override
    public void registerCar(CarData request, StreamObserver<CarData> responseObserver) {
        String licenseNumber = request.getLicenseNumber();
        String model = request.getModel();
        int year = request.getYear();
        Money price = request.getPrice();
        try {
            Car car = carBase.registerCar(licenseNumber, model, year, GrpcToModel.money(price));
            responseObserver.onNext(ModelToGrpc.car(car));
        } catch (PersistanceException e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getCar(CarId request, StreamObserver<CarData> responseObserver) {
        String licenseNumber = request.getLicenseNumber();
        try {
            Car car = carBase.getCar(licenseNumber);
            responseObserver.onNext(ModelToGrpc.car(car));
        } catch (PersistanceException e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllCars(EmptyMessage request, StreamObserver<CarsData> responseObserver) {
        try {
            List<Car> allCars = carBase.getAllCars();
            responseObserver.onNext(ModelToGrpc.cars(allCars));
        } catch (PersistanceException e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void removeCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        Car car = GrpcToModel.car(request);
        try {
            carBase.removeCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
        } catch (PersistanceException e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
