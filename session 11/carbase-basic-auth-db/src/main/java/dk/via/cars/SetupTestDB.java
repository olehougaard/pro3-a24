package dk.via.cars;

import dk.via.cars.data.DatabaseHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SetupTestDB {
    public static void main(String[] args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        DatabaseHelper<?> helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
        helper.executeUpdate("DELETE FROM car");
        helper.executeUpdate("DELETE FROM user_roles");
        helper.executeUpdate("DELETE FROM \"User\"");
        helper.executeUpdate("DELETE FROM roles");
        helper.executeUpdate("INSERT INTO car VALUES(?, ?, ?, ?, ?)", "AV 41 213", "Ford", 2014, 4999.99, "EUR");
        helper.executeUpdate("INSERT INTO roles VALUES ('admin'), ('registered_user')");
        helper.executeUpdate("INSERT INTO \"User\"(username, password) VALUES (?, ?)", "admin", encoder.encode("password"));
        helper.executeUpdate("INSERT INTO \"User\"(username, password) VALUES (?, ?)", "user", encoder.encode("abcd1234"));
        helper.executeUpdate("INSERT INTO User_Roles VALUES ('admin', 'admin'), ('admin', 'registered_user'), ('user', 'registered_user')");
    }
}
