package org.burgas.ticketservice.message;

public enum TicketMessages {

    TICKET_NOT_CREATED("Ticket was not created: {}");

    private final String message;

    TicketMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
