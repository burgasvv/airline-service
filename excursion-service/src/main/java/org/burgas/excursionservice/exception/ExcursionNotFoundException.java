package org.burgas.excursionservice.exception;

public class ExcursionNotFoundException extends RuntimeException {

    public ExcursionNotFoundException(String message) {
        super(message);
    }
}
