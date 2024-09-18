package dk.via.cars.grpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import dk.via.carbase.*;
import dk.via.cars.business.CarBase;
import dk.via.cars.data.DuplicateKeyException;
import dk.via.cars.data.NotFoundException;
import dk.via.cars.data.PersistanceException;
import dk.via.cars.model.Car;
import io.grpc.protobuf.StatusProto;
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
            responseObserver.onCompleted();
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            Status error = Status.newBuilder().setCode(Code.ALREADY_EXISTS_VALUE).setMessage("Duplicate license plate").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (PersistanceException e) {
            e.printStackTrace();
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Couldn't save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void getCar(CarId request, StreamObserver<CarData> responseObserver) {
        String licenseNumber = request.getLicenseNumber();
        try {
            Car car = carBase.getCar(licenseNumber);
            Status error = Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage("Car not found").build();
            responseObserver.onNext(ModelToGrpc.car(car));
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            Status error = Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage("Car not found").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (PersistanceException e) {
            e.printStackTrace();
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Couldn't read data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void getAllCars(EmptyMessage request, StreamObserver<CarsData> responseObserver) {
        try {
            List<Car> allCars = carBase.getAllCars();
            responseObserver.onNext(ModelToGrpc.cars(allCars));
        } catch (PersistanceException e) {
            e.printStackTrace();
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Couldn't read data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void removeCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        Car car = GrpcToModel.car(request);
        try {
            carBase.removeCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
        } catch (PersistanceException e) {
            e.printStackTrace();
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Couldn't write data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }
}
