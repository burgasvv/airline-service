package org.burgas.hotelbackend.exception;

public class AddressNotCreatedOrUpdatedException extends RuntimeException {

    public AddressNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
