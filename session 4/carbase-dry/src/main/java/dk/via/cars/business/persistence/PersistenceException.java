package dk.via.cars.business.persistence;

import java.sql.SQLException;

public class PersistenceException extends RuntimeException {
    public PersistenceException() {
    }

    public PersistenceException(SQLException cause) {
        super(cause);
    }
}
