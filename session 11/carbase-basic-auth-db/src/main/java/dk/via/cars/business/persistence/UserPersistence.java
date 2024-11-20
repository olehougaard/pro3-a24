package dk.via.cars.business.persistence;

import dk.via.cars.model.User;

import java.sql.SQLException;

public interface UserPersistence {
    public User read(String username) throws SQLException;
}
