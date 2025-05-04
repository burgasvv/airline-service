package org.burgas.hotelbackend.log;

public enum IdentityLogs {

    IDENTITY_FOUND_BEFORE_ACTIVATE_DEACTIVATE("Identity was found before activate or deactivate: {}"),
    IDENTITY_FOUND_BEFORE_ACTIVATE_DEACTIVATE_ASYNC("Identity was found before activate or deactivate async: {}"),
    IDENTITY_FOUND_BY_ID("Identity was found by id: {}"),
    IDENTITY_FOUND_BY_ID_ASYNC("Identity was found by id async: {}"),
    IDENTITY_FOUND_ALL("Identity was found in list of all: {}"),
    IDENTITY_FOUND_ALL_ASYNC("Identity was found in list of all async: {}");

    private final String logMessage;

    IdentityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
