package org.burgas.hotelbackend.exception;

public class CountryNotCreatedOrUpdatedException extends RuntimeException {

    public CountryNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
