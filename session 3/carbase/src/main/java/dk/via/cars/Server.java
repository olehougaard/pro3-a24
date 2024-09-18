package dk.via.cars;

import dk.via.cars.business.CarBase;
import dk.via.cars.data.CarDAO;
import dk.via.cars.data.CarDAOImplementation;
import dk.via.cars.data.DatabaseHelper;
import dk.via.cars.grpc.CarServiceImplementation;
import dk.via.cars.model.Car;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server {
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		DatabaseHelper<Car> db = new DatabaseHelper<Car>("jdbc:postgresql://localhost:5433/postgres?currentSchema=car_base", "postgres", "password");
		CarDAO dao = new CarDAOImplementation(db);
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
