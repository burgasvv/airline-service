package org.burgas.excursionservice.log;

public enum AuthorityLogs {

    AUTHORITY_FOUND_ALL("Authority was found in list of all: {}"),
    TRANSFORM_TO_AUTHORITY_RESPONSE("Transform to authority response: {}"),
    AUTHORITY_FOUND_ASYNC("Find authority async: {}"),
    TRANSFORM_TO_AUTHORITY_RESPONSE_ASYNC("Transform to authority response async: {}"),
    AUTHORITY_FOUND_BY_ID("Authority was found by id: {}"),
    AUTHORITY_DELETED_BY_ID("Authority was deleted by id: {}"),
    AUTHORITY_DELETED_BY_ID_ASYNC("Authority was deleted by id async: {}");

    private final String logMessage;

    AuthorityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
