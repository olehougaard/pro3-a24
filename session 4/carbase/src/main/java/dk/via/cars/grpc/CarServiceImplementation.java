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
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@GrpcService
public class CarServiceImplementation extends CarServiceGrpc.CarServiceImplBase {
    private final CarBase carBase;
    private static Logger logger = LoggerFactory.getLogger("Service");

    public CarServiceImplementation(CarBase carBase) {
        this.carBase = carBase;
    }

    @Override
    public void registerCar(CarData request, StreamObserver<CarData> responseObserver) {
        try {
            String licenseNumber = request.getLicenseNumber();
            String model = request.getModel();
            int year = request.getYear();
            Money price = request.getPrice();
            Car car = carBase.registerCar(licenseNumber, model, year, GrpcToModel.money(price));
            CarData response = ModelToGrpc.car(car);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (DuplicateKeyException e) {
            Status error = Status.newBuilder().setCode(Code.ALREADY_EXISTS_VALUE).setMessage("Duplicate license plate").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
            logger.error("Duplicate license plate", e);
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
            logger.error("Could not save", e);
        } catch (ValidationException | NumberFormatException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
            logger.error("Validation", e);
        }
    }

    @Override
    public void getCar(CarId request, StreamObserver<CarData> responseObserver) {
        try {
            String licenseNumber = request.getLicenseNumber();
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
            logger.error("Persistence", e);
        }
    }

    @Override
    public void updateCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        try {
            Car car = GrpcToModel.car(request);
            carBase.updateCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException | NumberFormatException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }

    @Override
    public void removeCar(CarData request, StreamObserver<EmptyMessage> responseObserver) {
        try {
            Car car = GrpcToModel.car(request);
            carBase.removeCar(car);
            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (PersistenceException e) {
            Status error = Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage("Could not save data").build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        } catch (ValidationException | NumberFormatException e) {
            Status error = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(e.getMessage()).build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(error));
        }
    }
}
