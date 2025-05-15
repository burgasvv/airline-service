package org.burgas.hotelbackend.exception;

public class PassportNotMatchesException extends RuntimeException {

    public PassportNotMatchesException(String message) {
        super(message);
    }
}
