package org.burgas.ticketservice.exception;

public class IdentityNotAuthenticatedException extends RuntimeException {

    public IdentityNotAuthenticatedException(String message) {
        super(message);
    }
}
