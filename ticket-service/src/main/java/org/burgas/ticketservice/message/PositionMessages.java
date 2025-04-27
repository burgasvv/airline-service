package org.burgas.ticketservice.message;

public enum PositionMessages {

    POSITION_NOT_CREATED("Position not created"),
    POSITION_DELETED("Должность успешно удалена"),
    POSITION_NOT_FOUND("Должность не найдена");

    private final String message;

    PositionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
