package org.burgas.ticketservice.message;

public enum EmployeeMessages {

    EMPLOYEE_NOT_CREATED("Аккаунт сотрудника не был создан"),
    PASSPORT_NOT_MATCHES("Паспорт не совпадает с шаблоном");

    private final String message;

    EmployeeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
