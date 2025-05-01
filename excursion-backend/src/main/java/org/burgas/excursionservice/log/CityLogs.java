package org.burgas.excursionservice.log;

public enum CityLogs {

    CITY_FOUND_OF_ALL("City was found in list of all: {}"),
    CITY_FOUND_OF_ALL_ASYNC("City was found in list of all async: {}"),
    CITY_FOUND_BY_ID("City was found by id: {}"),
    CITY_FOUND_BY_ID_ASYNC("City was found by id async: {}"),
    CITY_FOUND_BEFORE_DELETING("City was found before deleting: {}"),
    CITY_FOUND_BEFORE_DELETING_ASYNC("City was found before deleting: {}");

    private final String logMessage;

    CityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
