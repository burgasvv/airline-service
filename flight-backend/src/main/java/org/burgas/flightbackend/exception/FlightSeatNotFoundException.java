package org.burgas.flightbackend.exception;

public class FlightSeatNotFoundException extends RuntimeException {

    public FlightSeatNotFoundException(String message) {
        super(message);
    }
}
