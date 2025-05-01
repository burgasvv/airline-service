package org.burgas.flightbackend.message;

public enum TicketMessages {

    TICKET_NOT_FOUND("Билет не был найден в базе"),
    TICKET_NOT_CREATED("Билет не был создан");

    private final String message;

    TicketMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
