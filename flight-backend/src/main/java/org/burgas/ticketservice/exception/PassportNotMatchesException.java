package org.burgas.ticketservice.exception;

public class PassportNotMatchesException extends RuntimeException {

    public PassportNotMatchesException(String message) {
        super(message);
    }
}
