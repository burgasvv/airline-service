package org.burgas.flightbackend.message;

public enum PlaneMessages {

    PLANE_NOT_CREATED("Запись о модели самолета не была создана"),
    PLANE_NOT_FOUND("Модель самолета не была найдена");

    private final String message;

    PlaneMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
