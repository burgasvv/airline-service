package org.burgas.flightbackend.log;

public enum OrderedTicketLogs {

    ORDERED_TICKET_FOUND_BY_IDENTITY_ID("Ordered ticket was found by identity id: {}"),
    ORDERED_TICKET_FOUND_ALL("Ordered ticket was found in list of all: {}"),
    ORDERED_TICKET_FOUND_BY_ID("Ordered ticket was found byId: {}");

    private final String logMessage;

    OrderedTicketLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
