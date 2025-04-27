package org.burgas.ticketservice.message;

public enum DepartmentMessages {

    DEPARTMENT_NOT_FOUND("Отдела не найден"),
    DEPARTMENT_NOT_CREATED("Отдел не был создан"),
    DEPARTMENT_DELETED("Отдела успешно удален");

    private final String message;

    DepartmentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
