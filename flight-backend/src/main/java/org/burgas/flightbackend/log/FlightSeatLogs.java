package org.burgas.flightbackend.log;

public enum FlightSeatLogs {

    FLIGHT_SEAT_FOUND_BY_FLIGHT_ID("Flight seat was found by flight id: {}");

    private final String logMessage;

    FlightSeatLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
