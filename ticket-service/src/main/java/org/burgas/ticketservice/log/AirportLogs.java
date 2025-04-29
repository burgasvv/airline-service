package org.burgas.ticketservice.log;

public enum AirportLogs {

    AIRPORT_FOUND_ALL("Airport was found in list of all: {}"),
    AIRPORT_FOUND_ALL_ASYNC("Airport was found in list of all async: {}"),
    AIRPORT_FOUND_BY_COUNTRY_ID("Airport was found in list by country id: {}"),
    AIRPORT_FOUND_BY_COUNTRY_ID_ASYNC("Airport was found in list by country id async: {}"),
    AIRPORT_FOUND_BY_CITY_ID("Airport was found in list by city id: {}"),
    AIRPORT_FOUND_BY_CITY_ID_ASYNC("Airport was found in list by city id async: {}");

    private final String logMessage;

    AirportLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
