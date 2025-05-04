package org.burgas.hotelbackend.exception;

public class PhoneNotMatchesException extends RuntimeException {

    public PhoneNotMatchesException(String message) {
        super(message);
    }
}
