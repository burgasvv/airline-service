package org.burgas.hotelbackend.exception;

public class CityNotCreatedOrUpdatedException extends RuntimeException {

    public CityNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
