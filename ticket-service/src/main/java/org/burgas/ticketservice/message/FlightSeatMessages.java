package org.burgas.ticketservice.message;

public enum FlightSeatMessages {

    TICKET_FLIGHT_SEAT_NOT_MERGED("Класс пассажирского места и тип билета не совпадают"),
    FLIGHT_SEAT_NOT_FOUND("Пассажирское место не найдено");

    private final String logMessage;

    FlightSeatMessages(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
