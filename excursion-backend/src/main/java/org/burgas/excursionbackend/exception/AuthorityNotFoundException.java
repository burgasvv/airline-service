package org.burgas.excursionbackend.exception;

public class AuthorityNotFoundException extends RuntimeException {

    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
