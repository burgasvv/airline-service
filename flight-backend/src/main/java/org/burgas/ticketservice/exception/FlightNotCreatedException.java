package org.burgas.ticketservice.exception;

public class FlightNotCreatedException extends RuntimeException {

    public FlightNotCreatedException(String message) {
        super(message);
    }
}
