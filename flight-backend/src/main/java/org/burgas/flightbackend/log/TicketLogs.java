package org.burgas.flightbackend.log;

public enum TicketLogs {

    TICKET_FOUND_BY_FLIGHT_ID("Ticket was found by flight id: {}"),
    TICKET_WAS_FOUND_BY_ID("Ticket was found by id: {}"),
    TICKET_FOUND_ALL("Ticket was found in list of all: {}");

    private final String logMessage;

    TicketLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
