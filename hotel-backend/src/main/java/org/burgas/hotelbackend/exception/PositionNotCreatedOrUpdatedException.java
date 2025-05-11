package org.burgas.hotelbackend.exception;

public class PositionNotCreatedOrUpdatedException extends RuntimeException {

    public PositionNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
