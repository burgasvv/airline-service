package org.burgas.flightbackend.exception;

public class WrongIdentityException extends RuntimeException {

    public WrongIdentityException(String message) {
        super(message);
    }
}
