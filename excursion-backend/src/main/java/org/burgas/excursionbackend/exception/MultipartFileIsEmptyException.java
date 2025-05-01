package org.burgas.excursionbackend.exception;

public class MultipartFileIsEmptyException extends RuntimeException {

    public MultipartFileIsEmptyException(String message) {
        super(message);
    }
}
