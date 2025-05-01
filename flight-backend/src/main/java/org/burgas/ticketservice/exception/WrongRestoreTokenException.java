package org.burgas.ticketservice.exception;

public class WrongRestoreTokenException extends RuntimeException {

    public WrongRestoreTokenException(String message) {
        super(message);
    }
}
