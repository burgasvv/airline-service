package org.burgas.flightbackend.log;

public enum RequireAnswerLogs {

    REQUIRE_ANSWER_FOUND_ALL_BY_USER_ID("Require Answer was found in a list of all BY USER ID: {}"),
    REQUIRE_ANSWER_FOUND_ALL_BY_ADMIN_ID("Require Answer was found in a list of all BY USER ID: {}");

    private final String logMessage;

    RequireAnswerLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
