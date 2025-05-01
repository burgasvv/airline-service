package org.burgas.excursionservice.exception;

public class GuideNotFoundException extends RuntimeException {

    public GuideNotFoundException(String message) {
        super(message);
    }
}
