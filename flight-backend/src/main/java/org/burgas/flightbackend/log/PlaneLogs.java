package org.burgas.flightbackend.log;

public enum PlaneLogs {

    PLANE_FOUND_BY_FREE_ASYNC("Plane was found in list of all by free async: {}"),
    PLANE_FOUND_ALL_ASYNC("Plane was found in list of all async: {}"),
    PLANE_FOUND_BEFORE_DELETE("Plane was found before delete: {}"),
    PLANE_FOUND_BY_ID("Plane was found by id: {}"),
    PLANE_FOUND_BY_ID_ASYNC("Plane was found by id async: {}"),
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
