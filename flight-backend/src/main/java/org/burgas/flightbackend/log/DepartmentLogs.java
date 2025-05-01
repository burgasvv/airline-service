package org.burgas.flightbackend.log;

public enum DepartmentLogs {

    DEPARTMENT_FOUND_ALL("Department was found in list of all: {}"),
    DEPARTMENT_FOUND_ALL_ASYNC("Department was found in list of all async: {}"),
    DEPARTMENT_FOUND_BY_ID("Department was found by id: {}"),
    DEPARTMENT_FOUND_BY_ID_ASYNC("Department was found by id async: {}"),
    DEPARTMENT_FOUND_BEFORE_DELETE("Department was found by id before delete async: {}");

    private final String logMessage;

    DepartmentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
