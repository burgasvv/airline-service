package org.burgas.ticketservice.log;

public enum PlaneLogs {

    PLANE_FOUND_BY_ID("Plane was found by id: {}"),
    PLANE_FOUND_BY_FREE("Plane was found in list of all by free: {}"),
    PLANE_FOUND_ALL("Plane was found in list of all: {}");

    private final String logMessage;

    PlaneLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
