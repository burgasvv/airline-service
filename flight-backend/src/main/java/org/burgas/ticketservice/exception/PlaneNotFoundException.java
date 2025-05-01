package org.burgas.ticketservice.exception;

public class PlaneNotFoundException extends RuntimeException {

    public PlaneNotFoundException(String message) {
        super(message);
    }
}
