package org.burgas.excursionservice.log;

public enum GuideLogs {

    GUIDE_FOUND_ALL("Guide was found in list of all: {}"),
    GUIDE_FOUND_ALL_ASYNC("Guide was found in list of all async: {}"),
    GUIDE_FOUND_BY_ID("Guide was found by id: {}"),
    GUIDE_FOUND_BY_ID_ASYNC("Guide was found by id async: {}");

    private final String logMessage;

    GuideLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
