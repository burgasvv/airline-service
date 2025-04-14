package org.burgas.excursionservice.message;

public enum CityMessages {

    CITY_DELETED("Город был успешно удален"),
    CITY_NOT_FOUND("Указанный вами город не найден");

    private final String message;

    CityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
