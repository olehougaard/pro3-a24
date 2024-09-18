package dk.via.cars.data;

import java.sql.SQLException;

public class PersistenceException extends Exception {
    public PersistenceException() {
    }

    public PersistenceException(SQLException cause) {
        super(cause);
    }
}
