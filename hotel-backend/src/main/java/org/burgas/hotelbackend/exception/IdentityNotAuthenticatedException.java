package org.burgas.hotelbackend.exception;

public class IdentityNotAuthenticatedException extends RuntimeException {

    public IdentityNotAuthenticatedException(String message) {
        super(message);
    }
}
