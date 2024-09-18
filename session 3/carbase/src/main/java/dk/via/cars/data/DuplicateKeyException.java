package dk.via.cars.data;

import java.sql.SQLException;

public class DuplicateKeyException extends PersistenceException {
    public DuplicateKeyException(SQLException cause) {
        super(cause);
    }
}
