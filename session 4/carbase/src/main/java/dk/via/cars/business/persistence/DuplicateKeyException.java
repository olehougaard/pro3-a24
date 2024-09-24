package dk.via.cars.business.persistence;

import java.sql.SQLException;

public class DuplicateKeyException extends PersistenceException {
    public DuplicateKeyException() {
    }


    public DuplicateKeyException(SQLException cause) {
        super(cause);
    }
}
