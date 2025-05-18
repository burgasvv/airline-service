package org.burgas.hotelbackend.exception;

public class ClientNotCreatedOrUpdatedException extends RuntimeException {

    public ClientNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
