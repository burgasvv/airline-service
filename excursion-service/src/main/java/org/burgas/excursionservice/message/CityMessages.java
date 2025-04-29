package org.burgas.excursionservice.message;

public enum CityMessages {

    CITY_NOT_CREATED("Запись города не была создана"),
    CITY_DELETED("Город был успешно удален"),
    CITY_NOT_FOUND("Указанный вами город не найден"),
    IMAGE_UPLOADED("Фотография города с идентификатором %s успешно загружена"),
    IMAGE_UPLOADED_ASYNC("Фотография города с идентификатором %s успешно загружена async"),
    IMAGE_CHANGED("Фотография города с идентификатором %s успешно обновлена"),
    IMAGE_CHANGED_ASYNC("Фотография города с идентификатором %s успешно обновлена async"),
    IMAGE_DELETED("Фотография города с идентификатором %s успешно удалена"),
    IMAGE_DELETED_ASYNC("Фотография города с идентификатором %s успешно удалена async"),
    CITY_IMAGE_NOT_FOUND("Указанная фотография города не найдена");

    private final String message;

    CityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
