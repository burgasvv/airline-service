package org.burgas.ticketservice.exception;

public class IdentityWrongTokenException extends RuntimeException {

    public IdentityWrongTokenException(String message) {
        super(message);
    }
}
