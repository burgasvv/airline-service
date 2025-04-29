package org.burgas.ticketservice.log;

public enum EmployeeLogs {

    EMPLOYEE_FOUND_BEFORE_DELETE("Employee was found before delete: {}"),
    EMPLOYEE_FOUND_ALL("Employee was found in list of all: {}"),
    EMPLOYEE_FOUND_ALL_ASYNC("Employee was found in list of all async: {}"),
    EMPLOYEE_FOUND_BY_ID("Employee was found by id: {}"),
    EMPLOYEE_FOUND_BY_ID_ASYNC("Employee was found by id async: {}");

    private final String logMessage;

    EmployeeLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
