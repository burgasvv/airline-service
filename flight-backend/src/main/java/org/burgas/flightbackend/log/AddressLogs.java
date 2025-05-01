package org.burgas.flightbackend.log;

public enum AddressLogs {

    ADDRESS_FOUND_BEFORE_DELETE("Address was found before delete: {}"),
    ADDRESS_FOUND_ALL("Address was found in list of all: {}"),
    ADDRESS_FOUND_ALL_ASYNC("Address was found in list of all async: {}");

    private final String logMessage;

    AddressLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
