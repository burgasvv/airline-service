package org.burgas.hotelbackend.message;

public enum EmployeeMessages {

    EMPLOYEE_NOT_AUTHORIZED("Сотрудник не авторизован"),
    EMPLOYEE_IMAGE_UPLOADED("Изображение сотрудника с идентификатором %s было успешно загружено"),
    EMPLOYEE_IMAGE_CHANGED("Изображение сотрудника с идентификатором %s было успешно изменено"),
    EMPLOYEE_IMAGE_DELETED("Изображение сотрудника с идентификатором %s было успешно удалено"),
    EMPLOYEE_NOT_FOUND("Запись о сотруднике не была найдена"),
    EMPLOYEE_DELETED("Запись о сотруднике была успешно удалена"),
    EMPLOYEE_NOT_CREATED_OR_UPDATED("Запись о сотруднике не была создана или удалена"),
    PASSPORT_NOT_MATCHES("Паспорт не совпадает с шаблоном");

    private final String message;

    EmployeeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
