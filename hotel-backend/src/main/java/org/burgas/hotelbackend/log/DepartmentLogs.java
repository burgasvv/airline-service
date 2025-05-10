package org.burgas.hotelbackend.log;

public enum DepartmentLogs {

    DEPARTMENT_FOUND_BEFORE_DELETE("Department was found before delete: {}"),
    DEPARTMENT_FOUND_BEFORE_DELETE_ASYNC("Department was found before delete async: {}"),
    DEPARTMENT_CREATED_OR_UPDATED("Department was create or updated: {}"),
    DEPARTMENT_CREATED_OR_UPDATED_ASYNC("Department was create or updated async: {}"),
    DEPARTMENT_FOUND_BY_NAME("Department was found by name: {}"),
    DEPARTMENT_FOUND_BY_NAME_ASYNC("Department was found by name async: {}"),
    DEPARTMENT_FOUND_BY_FILIAL_ID("Department was found by filial id: {}"),
    DEPARTMENT_FOUND_BY_FILIAL_ID_ASYNC("Department was found by filial id async: {}"),
    DEPARTMENT_FOUND_BY_ID("Department was found by id: {}"),
    DEPARTMENT_FOUND_BY_ID_ASYNC("Department was found by id async: {}"),
    DEPARTMENT_FOUND_ALL("Department was found iin list of all: {}"),
    DEPARTMENT_FOUND_ALL_ASYNC("Department was found iin list of all async: {}");

    private final String logMessage;

    DepartmentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
