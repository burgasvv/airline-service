package org.burgas.ticketservice.message;

public enum FlightMessage {

    EMPLOYEE_ADDED_TO_FLIGHT("Сотрудник успешно добавлен на рейс"),
    EMPLOYEE_REMOVED_FROM_FLIGHT("Сотрудник успешно удален с рейса");

    private final String message;

    FlightMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
