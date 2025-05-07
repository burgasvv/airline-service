package org.burgas.hotelbackend.log;

public enum AddressLogs {

    ADDRESS_FOUND_BEFORE_DELETE("Address was found before delete: {}"),
    ADDRESS_FOUND_BEFORE_DELETE_ASYNC("Address was found before delete async: {}"),
    ADDRESS_CREATE_OR_UPDATE("Address was created or updated: {}"),
    ADDRESS_CREATE_OR_UPDATE_ASYNC("Address was created or updated async: {}"),
    ADDRESS_FOUND_ALL("Address was found in list of all: {}"),
    ADDRESS_FOUND_ALL_ASYNC("Address was found in list of all async: {}"),
    ADDRESS_FOUND_ALL_PAGES("Address was found in list of all pages: {}"),
    ADDRESS_FOUND_BY_ID("Address was found by id: {}"),
    ADDRESS_FOUND_BY_ID_ASYNC("Address was found by id async: {}");

    private final String logMessage;

    AddressLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
