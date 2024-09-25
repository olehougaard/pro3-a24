package dk.via.cars;

import dk.via.cars.business.CarBase;
import dk.via.cars.business.persistence.Persistence;
import dk.via.cars.data.CarDAO;
import dk.via.cars.data.DatabaseHelper;
import dk.via.cars.grpc.CarServiceImplementation;
import dk.via.cars.model.Car;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.sql.SQLException;

public class Server {
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		DatabaseHelper<Car> db = new DatabaseHelper<Car>("jdbc:postgresql://localhost:543/postgres?currentSchema=car_base", "postgres", "password");
		Persistence dao = new CarDAO(db);
		CarBase base = new CarBase(dao);
		CarServiceImplementation grpc = new CarServiceImplementation(base);

		io.grpc.Server server = ServerBuilder
				.forPort(9090)
				.addService(grpc)
				.build();
		server.start();
		System.out.println("Server running");

		server.awaitTermination();
	}
}
