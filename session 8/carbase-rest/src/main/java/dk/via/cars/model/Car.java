package dk.via.cars.model;

public class Car {
	private final String licenseNumber;
	private final String model;
	private final int year;
	private Money price;

	public Car(String licenseNumber, String model, int year, Money price) throws ValidationException {
		if (licenseNumber == null || licenseNumber.isEmpty()) throw new ValidationException("licenseNumber is required");
		if (model == null || model.isEmpty()) throw new ValidationException("model is required");
		if (price == null) throw new ValidationException("price is required");
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
