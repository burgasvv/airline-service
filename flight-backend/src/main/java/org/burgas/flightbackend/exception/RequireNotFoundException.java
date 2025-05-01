package org.burgas.flightbackend.exception;

public class RequireNotFoundException extends RuntimeException {

    public RequireNotFoundException(String message) {
        super(message);
    }
}
