package org.burgas.hotelbackend.exception;

public class IdentityStatusAlreadySetException extends RuntimeException {

    public IdentityStatusAlreadySetException(String message) {
        super(message);
    }
}
