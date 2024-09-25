package dk.via.cars.grpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import dk.via.carbase.*;
import dk.via.cars.business.CarSalesSystem;
import dk.via.cars.grpc.wrapper.GrpcWrapper;
import dk.via.cars.model.ValidationException;
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
    private final CarSalesSystem sales;
    private static final Logger logger = LoggerFactory.getLogger("Service");

    public CarServiceImplementation(CarSalesSystem sales) {
        this.sales = sales;
    }

    @Override
    public void registerCar(CarData req, StreamObserver<CarData> responseObserver) {
        GrpcWrapper
            .forFunction((CarData request) -> {
                String licenseNumber = request.getLicenseNumber();
                String model = request.getModel();
                int year = request.getYear();
                Money price = request.getPrice();
                Car car = sales.registerCar(licenseNumber, model, year, GrpcToModel.money(price));
                return ModelToGrpc.car(car);
            })
            .onException(DuplicateKeyException.class, Code.ALREADY_EXISTS_VALUE, "Duplicate license plate", logger::error)
            .onException(PersistenceException.class, Code.INTERNAL_VALUE, "Could not save data", logger::error)
            .onExceptions(ValidationException.class, NumberFormatException.class).doCatch(Code.INVALID_ARGUMENT_VALUE, Exception::getMessage, logger::error)
            .call(req, responseObserver);
    }

    @Override
    public void getCar(CarId req, StreamObserver<CarData> responseObserver) {
        GrpcWrapper
            .forFunction((CarId request) -> {
                Car car = sales.getCar(request.getLicenseNumber());
                return ModelToGrpc.car(car);
            })
            .onException(NotFoundException.class, Code.NOT_FOUND_VALUE, "Car not found", logger::error)
            .onException(PersistenceException.class, Code.INTERNAL_VALUE, "Could not read data", logger::error)
            .onException(ValidationException.class, Code.INVALID_ARGUMENT_VALUE, Exception::getMessage, logger::error)
            .call(req, responseObserver);
    }

    @Override
    public void getAllCars(EmptyMessage req, StreamObserver<CarsData> responseObserver) {
        GrpcWrapper
            .forFunction((EmptyMessage request) -> {
                List<Car> allCars = sales.getAllCars();
                return ModelToGrpc.cars(allCars);
            })
            .onException(PersistenceException.class, Code.INTERNAL_VALUE, "Could not read data", logger::error)
            .call(req, responseObserver);
    }

    @Override
    public void updateCar(CarData req, StreamObserver<EmptyMessage> responseObserver) {
        GrpcWrapper
            .forConsumer(EmptyMessage.newBuilder().build(), (CarData request) -> {
                sales.updateCar(GrpcToModel.car(request));
            })
            .onException(PersistenceException.class, Code.INTERNAL_VALUE, "Could not save data", logger::error)
            .onExceptions(ValidationException.class, NumberFormatException.class).doCatch(Code.INVALID_ARGUMENT_VALUE, Exception::getMessage, logger::error)
            .call(req, responseObserver);
    }

    @Override
    public void removeCar(CarData req, StreamObserver<EmptyMessage> responseObserver) {
        GrpcWrapper
            .forConsumer(EmptyMessage.newBuilder().build(), (CarData request) -> {
                sales.updateCar(GrpcToModel.car(request));
            })
            .onException(PersistenceException.class, Code.INTERNAL_VALUE, "Could not save data", logger::error)
            .onExceptions(ValidationException.class, NumberFormatException.class).doCatch(Code.INVALID_ARGUMENT_VALUE, Exception::getMessage, logger::error)
            .call(req, responseObserver);
    }
}
