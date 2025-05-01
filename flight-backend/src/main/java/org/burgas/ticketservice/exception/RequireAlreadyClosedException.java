package org.burgas.ticketservice.exception;

public class RequireAlreadyClosedException extends RuntimeException {

    public RequireAlreadyClosedException(String message) {
        super(message);
    }
}
