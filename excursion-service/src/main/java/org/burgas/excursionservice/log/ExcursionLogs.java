package org.burgas.excursionservice.log;

public enum ExcursionLogs {

    EXCURSION_FOUND_ALL("Excursion was found in list of all: {}"),
    EXCURSION_FOUND_ALL_ASYNC("Excursion was found in list of all async: {}"),
    EXCURSION_FOUND_ALL_GUIDE_ID("Excursion was found in list of all by guide id: {}"),
    EXCURSION_FOUND_ALL_GUIDE_ID_ASYNC("Excursion was found in list of all by guide id async: {}"),
    EXCURSION_BY_ID("Excursion was found by id: {}"),
    EXCURSION_BY_ID_ASYNC("Excursion was found by id async: {}"),
    EXCURSION_SAVED("Excursion was saved"),
    EXCURSION_SAVED_ASYNC("Excursion was saved async"),
    EXCURSION_FOUND_BEFORE_DELETE("Экскурсия была найдена перед удалением"),
    EXCURSION_FOUND_BEFORE_DELETE_ASYNC("Экскурсия была найдена перед удалением async"),
    EXCURSION_FOUND_BEFORE_UPLOAD_IMAGE("Экскурсия была найдена перед загрузкой изображения"),
    EXCURSION_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC("Экскурсия была найдена перед загрузкой изображения async"),
    EXCURSION_FOUND_BEFORE_CHANGE_IMAGE("Экскурсия была найдена перед сменой изображения"),
    EXCURSION_FOUND_BEFORE_CHANGE_IMAGE_ASYNC("Экскурсия была найдена перед сменой изображения async"),
    EXCURSION_FOUND_BEFORE_DELETE_IMAGE("Экскурсия была найдена перед сменой изображения"),
    EXCURSION_FOUND_BEFORE_DELETE_IMAGE_ASYNC("Экскурсия была найдена перед сменой изображения async");

    private final String logMessage;

    ExcursionLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
