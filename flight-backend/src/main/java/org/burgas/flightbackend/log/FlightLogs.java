package org.burgas.flightbackend.log;

public enum FlightLogs {

    FLIGHT_FOUND_BY_ID("Flight was found by id: {}"),
    FLIGHT_FOUND_BY_DEPARTURE_AND_ARRIVAL_AND_DATE("Flight was found in list of all by departure and arrival and date: {}"),
    FLIGHT_FOUND_BY_DEPARTURE_AND_ARRIVAL("Flight was found in list of all by departure and arrival: {}"),
    FLIGHT_FOUND_ALL("Flight was found in list of all: {}");

    private final String logMessage;

    FlightLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
