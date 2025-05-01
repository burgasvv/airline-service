package org.burgas.flightbackend.exception;

public class IdentityWrongTokenException extends RuntimeException {

    public IdentityWrongTokenException(String message) {
        super(message);
    }
}
