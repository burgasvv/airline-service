package org.burgas.ticketservice.exception;

public class WrongFileFormatException extends RuntimeException {

    public WrongFileFormatException(String message) {
        super(message);
    }
}
