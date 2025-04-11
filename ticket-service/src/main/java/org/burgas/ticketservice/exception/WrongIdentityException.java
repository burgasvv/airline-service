package org.burgas.ticketservice.exception;

public class WrongIdentityException extends RuntimeException {

    public WrongIdentityException(String message) {
        super(message);
    }
}
