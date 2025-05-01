package org.burgas.excursionbackend.exception;

public class SightNotFoundException extends RuntimeException {

    public SightNotFoundException(String message) {
        super(message);
    }
}
