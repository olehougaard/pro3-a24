package dk.via.cars.data;

import java.sql.SQLException;

public class PersistanceException extends Exception {
    public PersistanceException(SQLException cause) {
        super(cause);
    }
}
