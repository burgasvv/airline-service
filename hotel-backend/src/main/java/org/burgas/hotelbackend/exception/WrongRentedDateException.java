package org.burgas.hotelbackend.exception;

public class WrongRentedDateException extends RuntimeException {

    public WrongRentedDateException(String message) {
        super(message);
    }
}
