package org.burgas.ticketservice.exception;

public class FlightSeatNotFoundException extends RuntimeException {

    public FlightSeatNotFoundException(String message) {
        super(message);
    }
}
