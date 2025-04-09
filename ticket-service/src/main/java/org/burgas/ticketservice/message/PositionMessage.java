package org.burgas.ticketservice.message;

public enum PositionMessage {

    POSITION_DELETED("Должность успешно удалена"),
    POSITION_NOT_FOUND("Должность не найдена");

    private final String message;

    PositionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
