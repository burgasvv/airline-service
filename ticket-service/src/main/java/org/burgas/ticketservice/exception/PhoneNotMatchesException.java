package org.burgas.ticketservice.exception;

public class PhoneNotMatchesException extends RuntimeException {

    public PhoneNotMatchesException(String message) {
        super(message);
    }
}
