package org.burgas.hotelbackend.exception;

public class EmployeeNotAuthorizedException extends RuntimeException {

    public EmployeeNotAuthorizedException(String message) {
        super(message);
    }
}
