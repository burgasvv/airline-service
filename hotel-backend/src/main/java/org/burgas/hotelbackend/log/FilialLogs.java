package org.burgas.hotelbackend.log;

public enum FilialLogs {

    FILIAL_FOUND_BEFORE_UPLOAD_IMAGE("Filial was found before upload image: {}"),
    FILIAL_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC("Filial was found before upload image async: {}"),
    FILIAL_FOUND_BEFORE_CHANGE_IMAGE("Filial was found before change image: {}"),
    FILIAL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC("Filial was found before change image async: {}"),
    FILIAL_FOUND_BEFORE_DELETE_IMAGE("Filial was found before delete image: {}"),
    FILIAL_FOUND_BEFORE_DELETE_IMAGE_ASYNC("Filial was found before delete image async: {}"),
    FILIAL_FOUND_BEFORE_DELETE("Filial was found before delete: {}"),
    FILIAL_FOUND_BEFORE_DELETE_ASYNC("Filial was found before delete async: {}"),
    FILIAL_CREATED_OR_UPDATED("Filial was created or updated: {}"),
    FILIAL_CREATED_OR_UPDATED_ASYNC("Filial was created or updated async: {}"),
    FILIAL_FOUND_ALL("Filial was found in list of all: {}"),
    FILIAL_FOUND_ALL_ASYNC("Filial was found in list of all async: {}"),
    FILIAL_FOUND_ALL_PAGES("Filial was found in list of all pages: {}"),
    FILIAL_FOUND_BY_ID("Filial was found by id: {}"),
    FILIAL_FOUND_BY_ID_ASYNC("Filial was found by id async: {}");

    private final String logMessage;

    FilialLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
