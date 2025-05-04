package org.burgas.hotelbackend.exception;

public class MultipartFileIsEmptyException extends RuntimeException {

    public MultipartFileIsEmptyException(String message) {
        super(message);
    }
}
