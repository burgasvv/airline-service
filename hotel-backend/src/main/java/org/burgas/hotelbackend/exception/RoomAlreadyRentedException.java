package org.burgas.hotelbackend.exception;

public class RoomAlreadyRentedException extends RuntimeException {

    public RoomAlreadyRentedException(String message) {
        super(message);
    }
}
