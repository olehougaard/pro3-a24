package dk.via.cars.grpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import dk.via.carbase.*;
import dk.via.cars.business.CarBase;
import dk.via.cars.business.ValidationException;
import dk.via.cars.business.persistence.DuplicateKeyException;
import dk.via.cars.business.persistence.NotFoundException;
import dk.via.cars.business.persistence.PersistenceException;
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
            Status error = Status.newBuilder().setCode(Code.ALREADY_EXISTS_VALUE).setMessage("Duplicate license plate").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void getCar(CarId request, StreamObserver<CarData> responseObserver) {
        String licenseNumber = request.getLicenseNumber();
        try {
            Car car = carBase.getCar(licenseNumber);
            responseObserver.onNext(ModelToGrpc.car(car));
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            Status error = Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage("Car not found").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not read data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void getAllCars(EmptyMessage request, StreamObserver<CarsData> responseObserver) {
        try {
            List<Car> allCars = carBase.getAllCars();
            responseObserver.onNext(ModelToGrpc.cars(allCars));
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            Status error = Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage("Car not found").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not read data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void updateCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        Car car = GrpcToModel.car(request);
        try {
            carBase.updateCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void removeCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        Car car = GrpcToModel.car(request);
        try {
            carBase.removeCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }
}
