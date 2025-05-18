package org.burgas.hotelbackend.exception;

public class RoomByNumberAndFilialIdAlreadyExistsException extends RuntimeException {

    public RoomByNumberAndFilialIdAlreadyExistsException(String message) {
        super(message);
    }
}
