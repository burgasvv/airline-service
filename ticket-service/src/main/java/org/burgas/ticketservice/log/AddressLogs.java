package org.burgas.ticketservice.log;

public enum AddressLogs {

    ADDRESS_FOUND_ALL("Address was found in list of all: {}");

    private final String logMessage;

    AddressLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
