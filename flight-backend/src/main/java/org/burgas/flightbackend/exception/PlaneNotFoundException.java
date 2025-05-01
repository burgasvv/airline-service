package org.burgas.flightbackend.exception;

public class PlaneNotFoundException extends RuntimeException {

    public PlaneNotFoundException(String message) {
        super(message);
    }
}
