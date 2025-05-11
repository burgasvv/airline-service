package org.burgas.hotelbackend.exception;

public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException(String message) {
        super(message);
    }
}
