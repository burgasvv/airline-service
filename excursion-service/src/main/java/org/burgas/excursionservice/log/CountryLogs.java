package org.burgas.excursionservice.log;

public enum CountryLogs {

    COUNTRY_FOUND_ALL("Country was found from list of all: {}"),
    COUNTRY_FOUND_ALL_ASYNC("Country was found from list of all async: {}"),
    COUNTRY_FOUND_BY_ID("Country was found by id: {}"),
    COUNTRY_FOUND_BY_ID_ASYNC("Country was found by id async: {}"),
    COUNTRY_FOUND_BEFORE_DELETING("Country was found before deleting: {}");

    private final String logMessage;

    CountryLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
