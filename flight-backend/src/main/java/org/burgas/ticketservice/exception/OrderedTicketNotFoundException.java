package org.burgas.ticketservice.exception;

public class OrderedTicketNotFoundException extends RuntimeException {

    public OrderedTicketNotFoundException(String message) {
        super(message);
    }
}
