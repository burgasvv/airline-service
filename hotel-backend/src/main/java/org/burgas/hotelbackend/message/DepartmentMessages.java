package org.burgas.hotelbackend.message;

public enum DepartmentMessages {

    DEPARTMENT_DELETED("Запись о департаменте успешно удалена"),
    DEPARTMENT_NOT_FOUND("Запись о департаменте не найдена"),
    DEPARTMENT_NOT_CREATED_OR_UPDATED("Запись о департаменте не была создана или изменена");

    private final String message;

    DepartmentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
