package org.burgas.flightbackend.exception;

public class PassportNotMatchesException extends RuntimeException {

    public PassportNotMatchesException(String message) {
        super(message);
    }
}
