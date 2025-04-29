package org.burgas.ticketservice.message;

public enum FilialDepartmentMessages {

    FILIAL_DEPARTMENT_NOT_CREATED("Филиал департамент не был создан"),
    FILIAL_DEPARTMENT_NOT_FOUND("Филиал департамент не найден"),
    FILIAL_DEPARTMENT_DELETED("Филиал департамент удален"),
    FILIAL_DEPARTMENT_DELETED_ASYNC("Филиал департамент удален async");

    private final String message;

    FilialDepartmentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
