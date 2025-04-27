package org.burgas.ticketservice.log;

public enum EmployeeLogs {

    EMPLOYEE_FOUND_ALL("Employee was found in list of all: {}"),
    EMPLOYEE_FOUND_BY_ID("Employee was found by id: {}");

    private final String logMessage;

    EmployeeLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
