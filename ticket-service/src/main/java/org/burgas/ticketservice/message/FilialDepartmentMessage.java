package org.burgas.ticketservice.message;

public enum FilialDepartmentMessage {

    FILIAL_DEPARTMENT_NOT_FOUND("Филиал департамент не найден"),
    FILIAL_DEPARTMENT_DELETED("Филиал департамент удален");

    private final String message;

    FilialDepartmentMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
