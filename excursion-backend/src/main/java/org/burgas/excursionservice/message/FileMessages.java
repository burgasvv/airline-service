package org.burgas.excursionservice.message;

public enum FileMessages {

    MULTIPART_IS_EMPTY("Загруженный вами файл пуст");

    private final String message;

    FileMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
