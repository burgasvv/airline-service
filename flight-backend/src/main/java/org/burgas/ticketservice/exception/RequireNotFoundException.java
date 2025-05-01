package org.burgas.ticketservice.exception;

public class RequireNotFoundException extends RuntimeException {

    public RequireNotFoundException(String message) {
        super(message);
    }
}
