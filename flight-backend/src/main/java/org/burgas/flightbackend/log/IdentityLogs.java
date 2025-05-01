package org.burgas.flightbackend.log;

public enum IdentityLogs {

    IDENTITY_FOUND_BY_USERNAME("Identity was found by username"),
    IDENTITY_FOUND_BY_ID("Identity was found by id: {}"),
    IDENTITY_FOUND_ALL("Identity was found in list of all: {}");

    private final String logMessage;

    IdentityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
