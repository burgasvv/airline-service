package org.burgas.hotelbackend.exception;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message);
    }
}
