package org.burgas.ticketservice.message;

public enum EmployeeMessage {

    PASSPORT_NOT_MATCHES("Паспорт не совпадает с шаблоном");

    private final String message;

    EmployeeMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
