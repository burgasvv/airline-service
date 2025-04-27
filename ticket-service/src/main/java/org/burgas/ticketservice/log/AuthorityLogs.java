package org.burgas.ticketservice.log;

public enum AuthorityLogs {

    AUTHORITY_FOUND_ALL("Authority was found in list od all: {}"),
    AUTHORITY_FOUND_BY_ID("Authority was found by id: {}"),
    AUTHORITY_DELETED_BY_ID("Authority was deleted by id: {}");

    private final String logMessage;

    AuthorityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
