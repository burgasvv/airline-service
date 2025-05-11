package org.burgas.hotelbackend.message;

public enum PositionMessages {

    POSITION_DELETED("Запись о должности была успешно удалена"),
    POSITION_NOT_FOUND("Запись о должности не была найдена"),
    POSITION_NOT_CREATED_OR_UPDATED("Запись о должности не была создана или изменена");

    private final String message;

    PositionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
