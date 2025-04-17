package org.burgas.excursionservice.exception;

public class SightNotFoundException extends RuntimeException {

    public SightNotFoundException(String message) {
        super(message);
    }
}
