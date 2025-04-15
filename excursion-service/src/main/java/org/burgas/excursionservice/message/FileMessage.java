package org.burgas.excursionservice.message;

public enum FileMessage {

    MULTIPART_IS_EMPTY("Загруженный вами файл пуст");

    private final String message;

    FileMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
