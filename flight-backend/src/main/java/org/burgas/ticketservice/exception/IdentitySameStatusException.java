package org.burgas.ticketservice.exception;

public class IdentitySameStatusException extends RuntimeException {

    public IdentitySameStatusException(String message) {
        super(message);
    }
}
