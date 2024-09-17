package dk.via.cars.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Car {
	private final String licenseNumber;
	private final String model;
	private final int year;
	private Money price;

	public Car(String licenseNumber, String model, int year, Money price) {
		this.licenseNumber = licenseNumber;
		this.model = model;
		this.year = year;
		this.price = price;
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public String getModel() {
		return model;
	}

	public int getYear() {
		return year;
	}

	public Money getPrice() {
		return price;
	}
	
	public void setPrice(Money price) {
		this.price = price;
	}
}
