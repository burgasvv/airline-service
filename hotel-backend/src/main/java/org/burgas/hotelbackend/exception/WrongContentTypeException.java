package org.burgas.hotelbackend.exception;

public class WrongContentTypeException extends RuntimeException {

    public WrongContentTypeException(String message) {
        super(message);
    }
}
