package org.burgas.excursionbackend.log;

public enum SightLogs {

    SIGHT_FOUND_ALL("Sight was found in set of all: {}"),
    SIGHT_FOUND_ALL_ASYNC("Sight was found in set of all async: {}"),
    SIGHT_FOUND_BY_ID("Sight was found by id: {}"),
    SIGHT_FOUND_BY_ID_ASYNC("Sight was found by id async: {}");

    private final String logMessage;

    SightLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
