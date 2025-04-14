package org.burgas.excursionservice.exception;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String message) {
        super(message);
    }
}
