package org.burgas.flightbackend.exception;

public class AirportNotCreatedException extends RuntimeException {

    public AirportNotCreatedException(String message) {
        super(message);
    }
}
