package org.burgas.excursionservice.message;

public enum CountryMessages {

    COUNTRY_DELETED("Страна была успешно удалена"),
    COUNTRY_NOT_FOUND("Страна не найдена");

    private final String message;

    CountryMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
