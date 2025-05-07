package org.burgas.hotelbackend.log;

public enum CityLogs {

    CITY_FOUND_BEFORE_DELETE("City was found before delete: {}"),
    CITY_FOUND_BEFORE_DELETE_ASYNC("City was found before delete async: {}"),
    CITY_CREATED_OR_UPDATED("City was created or updated: {}"),
    CITY_CREATED_OR_UPDATED_ASYNC("City was created or updated async: {}"),
    CITY_FOUND_BY_ID("City was found by id: {}"),
    CITY_FOUND_BY_ID_ASYNC("City was found by id async: {}"),
    CITY_FOUND_BY_NAME("City was found by id: {}"),
    CITY_FOUND_BY_NAME_ASYNC("City was found by id async: {}"),
    CITY_FOUND_ALL("City was found in list of all: {}"),
    CITY_FOUND_ALL_ASYNC("City was found in list of all async: {}");

    private final String logMessage;

    CityLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
