package org.burgas.flightbackend.exception;

public class WrongRestoreTokenException extends RuntimeException {

    public WrongRestoreTokenException(String message) {
        super(message);
    }
}
