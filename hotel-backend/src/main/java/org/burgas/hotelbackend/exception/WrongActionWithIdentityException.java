package org.burgas.hotelbackend.exception;

public class WrongActionWithIdentityException extends RuntimeException {

    public WrongActionWithIdentityException(String message) {
        super(message);
    }
}
