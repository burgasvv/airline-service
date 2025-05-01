package org.burgas.flightbackend.exception;

public class OrderedTicketNotFoundException extends RuntimeException {

    public OrderedTicketNotFoundException(String message) {
        super(message);
    }
}
