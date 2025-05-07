package org.burgas.hotelbackend.message;

public enum CountryMessages {

    COUNTRY_DELETED("Запись о стране успешно удалена"),
    COUNTRY_DELETED_ASYNC("Запись о стране успешно удалена async"),
    COUNTRY_NOT_FOUND("Запись о стране не была найдена"),
    COUNTRY_NOT_CREATED_OR_UPDATED("Запись о стране не была создана или изменена"),
    COUNTRY_NOT_CREATED_OR_UPDATED_ASYNC("Запись о стране не была создана или изменена async");

    private final String message;

    CountryMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
