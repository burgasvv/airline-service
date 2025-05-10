package org.burgas.hotelbackend.exception;

public class DepartmentNotCreatedOrUpdatedException extends RuntimeException {

    public DepartmentNotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
