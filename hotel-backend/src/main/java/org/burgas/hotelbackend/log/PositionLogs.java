package org.burgas.hotelbackend.log;

public enum PositionLogs {

    POSITION_FOUND_BEFORE_DELETE("Position was found before delete: {}"),
    POSITION_FOUND_BEFORE_DELETE_ASYNC("Position was found before delete async: {}"),
    POSITION_CREATED_OR_UPDATED("Position was created or updated: {}"),
    POSITION_CREATED_OR_UPDATED_ASYNC("Position was created or updated async: {}"),
    POSITION_FOUND_ALL("Position was found in list of all: {}"),
    POSITION_FOUND_ALL_ASYNC("Position was found in list of all async: {}"),
    POSITION_FOUND_BY_ID("Position was found by id: {}"),
    POSITION_FOUND_BY_ID_ASYNC("Position was found by id async: {}");

    private final String logMessage;

    PositionLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
