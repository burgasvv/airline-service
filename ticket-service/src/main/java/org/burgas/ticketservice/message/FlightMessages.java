package org.burgas.ticketservice.message;

public enum FlightMessages {

    FLIGHT_STARTS("Рейс с идентификатором %s стартовал"),
    FLIGHT_COMPLETE("Рейс с идентификатором %s завершен"),
    FLIGHT_NOT_FOUND("Рейс не был найден"),
    FLIGHT_NOT_CREATED("Рейс не был создан"),
    FLIGHT_NOT_TRANSFORMED("Рейс не был преобразован"),
    EMPLOYEE_ADDED_TO_FLIGHT("Сотрудник успешно добавлен на рейс"),
    EMPLOYEE_REMOVED_FROM_FLIGHT("Сотрудник успешно удален с рейса");

    private final String message;

    FlightMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
