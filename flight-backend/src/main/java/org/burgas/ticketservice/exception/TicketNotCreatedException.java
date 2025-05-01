package org.burgas.ticketservice.exception;

public class TicketNotCreatedException extends RuntimeException {

    public TicketNotCreatedException(String message) {
        super(message);
    }
}
