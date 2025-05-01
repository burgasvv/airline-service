package org.burgas.flightbackend.message;

public enum EmployeeMessages {

    EMPLOYEE_DELETED_ASYNC("Аккаунт сотрудника был удален async"),
    EMPLOYEE_NOT_CREATED("Аккаунт сотрудника не был создан"),
    EMPLOYEE_NOT_FOUND("Аккаунт сотрудника не был найден"),
    EMPLOYEE_DELETED("Аккаунт сотрудника был удален"),
    PASSPORT_NOT_MATCHES("Паспорт не совпадает с шаблоном");

    private final String message;

    EmployeeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
