package org.burgas.excursionbackend.exception;

public class GuideNotFoundException extends RuntimeException {

    public GuideNotFoundException(String message) {
        super(message);
    }
}
