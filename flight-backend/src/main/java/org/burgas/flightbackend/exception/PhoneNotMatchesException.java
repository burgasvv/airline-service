package org.burgas.flightbackend.exception;

public class PhoneNotMatchesException extends RuntimeException {

    public PhoneNotMatchesException(String message) {
        super(message);
    }
}
