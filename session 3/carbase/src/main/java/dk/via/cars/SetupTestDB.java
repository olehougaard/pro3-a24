package dk.via.cars;

import dk.via.cars.data.DatabaseHelper;
import dk.via.cars.model.Car;

public class SetupTestDB {
    public static void main(String[] args) throws Exception {
        DatabaseHelper<Car> helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
        helper.executeUpdate("DELETE FROM car");
        helper.executeUpdate("INSERT INTO car VALUES(?, ?, ?, ?, ?)", "AV 41 213", "Ford", 2014, 4999.99, "EUR");
    }
}
