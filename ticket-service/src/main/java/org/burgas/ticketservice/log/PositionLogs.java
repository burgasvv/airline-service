package org.burgas.ticketservice.log;

public enum PositionLogs {

    POSITION_DELETED_LOG("Position was successfully deleted"),
    POSITION_FOUND_BEFORE_DELETE("Position was found by ide before deleting"),
    POSITION_FOUND_BY_ID("Position was found by id: {}"),
    POSITION_FOUND_ALL("Position wa found in list of all: {}");

    private final String logMessage;

    PositionLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
