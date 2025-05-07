package org.burgas.hotelbackend.log;

public enum CountryLogs {

    COUNTRY_CREATED_OR_UPDATED("Country was created or updated: {}"),
    COUNTRY_CREATED_OR_UPDATED_ASYNC("Country was created or updated async: {}"),
    COUNTRY_FOUND_BY_ID("Country was found by id: {}"),
    COUNTRY_FOUND_BY_ID_ASYNC("Country was found by id async: {}"),
    COUNTRY_FOUND_BY_NAME("Country was found by name: {}"),
    COUNTRY_FOUND_BY_NAME_ASYNC("Country was found by name async: {}"),
    COUNTRY_FOUND_BEFORE_DELETE("Country was before delete: {}"),
    COUNTRY_FOUND_BEFORE_DELETE_ASYNC("Country was before delete async: {}"),
    COUNTRY_FOUND_ALL("Country was found in list of all: {}"),
    COUNTRY_FOUND_ALL_ASYNC("Country was found in list of all async: {}");

    private final String logMessage;

    CountryLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
