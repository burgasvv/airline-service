package org.burgas.flightbackend.exception;

public class RequireAlreadyClosedException extends RuntimeException {

    public RequireAlreadyClosedException(String message) {
        super(message);
    }
}
