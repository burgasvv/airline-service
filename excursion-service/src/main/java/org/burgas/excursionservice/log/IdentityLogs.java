package org.burgas.excursionservice.log;

public enum IdentityLogs {

    IDENTITY_FOUND_OF_ALL("Identity was found in list of all: {}"),
    IDENTITY_FOUND_OF_ALL_ASYNC("Identity was found in list of all async: {}"),
    IDENTITY_FOUND_BY_ID("Identity was found by id: {}"),
    IDENTITY_FOUND_BY_ID_ASYNC("Identity was found by id async: {}"),
    IDENTITY_SAVED("Identity was saved: {}"),
    IDENTITY_FOUND_BEFORE_DELETE("Identity was found before delete: {}"),
    IDENTITY_FOUND_BEFORE_DELETE_ASYNC("Identity was found before delete async: {}"),
    IDENTITY_WAS_DELETED("Identity was deleted"),
    IDENTITY_FOUND_CONTROL("Identity was found for control: {}"),
    IDENTITY_FOUND_CONTROL_ASYNC("Identity was found for control async: {}");

    private final String logMessage;

    IdentityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
