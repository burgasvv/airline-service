package org.burgas.hotelbackend.exception;

public class RoomNotCreatedOrUpdatedException extends RuntimeException {

    public RoomNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
