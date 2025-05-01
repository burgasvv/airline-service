package org.burgas.flightbackend.exception;

public class AuthorityNotFoundException extends RuntimeException {

    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
