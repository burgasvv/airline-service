package org.burgas.ticketservice.log;

public enum FilialDepartmentLogs {

    FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID("Filial department was found by filial department id: {}"),
    FILIAL_DEPARTMENT_FOUND_ALL("Filial department was found in list od all: {}");

    private final String logMessage;

    FilialDepartmentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
