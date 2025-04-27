package org.burgas.ticketservice.log;

public enum AirportLogs {

    AIRPORT_FOUND_ALL("Airport was found in list of all: {}"),
    AIRPORT_FOUND_BY_COUNTRY_ID("Airport was found in list by country id: {}"),
    AIRPORT_FOUND_BY_CITY_ID("Airport was found in list by city id: {}");

    private final String logMessage;

    AirportLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
