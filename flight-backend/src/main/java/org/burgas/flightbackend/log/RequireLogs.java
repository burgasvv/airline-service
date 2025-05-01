package org.burgas.flightbackend.log;

public enum RequireLogs {

    REQUIRE_FOUND_BY_USER_ID("Require was found in list of all by userId: {}"),
    REQUIRE_FOUND_BY_ADMIN_ID("Require was found in list of all by adminId: {}"),
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
