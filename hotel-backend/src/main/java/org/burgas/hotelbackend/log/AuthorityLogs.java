package org.burgas.hotelbackend.log;

public enum AuthorityLogs {

    AUTHORITY_FOUND_BEFORE_DELETE("Authority was found before delete"),
    AUTHORITY_FOUND_ALL("Authority was found in list of all: {}"),
    AUTHORITY_FOUND_ALL_ASYNC("Authority was found in list of all async: {}"),
    AUTHORITY_FOUND_BY_ID("Authority was found by id: {}"),
    AUTHORITY_FOUND_BY_ID_ASYNC("Authority was found by id async: {}");

    private final String logMessage;

    AuthorityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
