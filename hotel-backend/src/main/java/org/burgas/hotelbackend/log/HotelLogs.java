package org.burgas.hotelbackend.log;

public enum HotelLogs {

    HOTEL_FOUND_BEFORE_UPLOAD_IMAGE("Hotel was found before upload image: {}"),
    HOTEL_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC("Hotel was found before upload image async: {}"),
    HOTEL_FOUND_BEFORE_CHANGE_IMAGE("Hotel was found before change image: {}"),
    HOTEL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC("Hotel was found before change image async: {}"),
    HOTEL_FOUND_BEFORE_DELETE_IMAGE("Hotel was found before delete image: {}"),
    HOTEL_FOUND_BEFORE_DELETE_IMAGE_ASYNC("Hotel was found before delete image async: {}"),
    HOTEL_FOUND_BEFORE_DELETE("Hotel was found before delete: {}"),
    HOTEL_FOUND_BEFORE_DELETE_ASYNC("Hotel was found before delete async: {}"),
    HOTEL_CREATED_OR_UPDATED("Hotel was created or updated: {}"),
    HOTEL_CREATED_OR_UPDATED_ASYNC("Hotel was created or updated: {}"),
    HOTEL_FOUND_ALL("Hotel was found in list of all: {}"),
    HOTEL_FOUND_ALL_PAGES("Hotel was found in list of all pages: {}"),
    HOTEL_FOUND_ALL_ASYNC("Hotel was found in list of all async: {}"),
    HOTEL_FOUND_BY_ID("Hotel was found by id: {}"),
    HOTEL_FOUND_BY_ID_ASYNC("Hotel was found by id async: {}"),
    HOTEL_FOUND_BY_NAME("Hotel was found by name: {}"),
    HOTEL_FOUND_BY_NAME_ASYNC("Hotel was found by name async: {}");

    private final String logMessage;

    HotelLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
