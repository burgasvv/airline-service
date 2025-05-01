package org.burgas.flightbackend.log;

public enum FilialDepartmentLogs {

    FILIAL_DEPARTMENT_FOUND_ALL_ASYNC("Filial department was found in list of all async: {}"),
    FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID("Filial department was found by filial department id: {}"),
    FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID_ASYNC("Filial department was found by filial department id async: {}"),
    FILIAL_DEPARTMENT_FOUND_ALL("Filial department was found in list od all: {}");

    private final String logMessage;

    FilialDepartmentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
