package org.burgas.ticketservice.log;

public enum RequireLogs {

    REQUIRE_FOUND_BY_ID("Require was found by id: {}"),
    REQUIRE_FOUND_ALL_BY_CLOSED("Require was found in list of all by closed: {}");

    private final String logMessage;

    RequireLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
