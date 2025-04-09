package org.burgas.ticketservice.message;

public enum DepartmentMessage {

    DEPARTMENT_NOT_FOUND("Отдела не найден"),
    DEPARTMENT_DELETED("Отдела успешно удален");

    private final String message;

    DepartmentMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
