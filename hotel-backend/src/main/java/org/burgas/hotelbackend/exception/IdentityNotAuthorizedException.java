package org.burgas.hotelbackend.exception;

public class IdentityNotAuthorizedException extends RuntimeException {

    public IdentityNotAuthorizedException(String message) {
        super(message);
    }
}
