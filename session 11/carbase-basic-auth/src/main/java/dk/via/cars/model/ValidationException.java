package dk.via.cars.model;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
