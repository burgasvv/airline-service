package org.burgas.ticketservice.log;

public enum DepartmentLogs {

    DEPARTMENT_FOUND_ALL("Department was found in list of all: {}"),
    DEPARTMENT_FOUND_BY_ID("Department was found by id: {}");

    private final String logMessage;

    DepartmentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
