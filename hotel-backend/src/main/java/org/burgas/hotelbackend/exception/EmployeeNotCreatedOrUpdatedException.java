package org.burgas.hotelbackend.exception;

public class EmployeeNotCreatedOrUpdatedException extends RuntimeException {

    public EmployeeNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
