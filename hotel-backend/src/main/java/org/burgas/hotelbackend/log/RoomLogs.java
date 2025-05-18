package org.burgas.hotelbackend.log;

public enum RoomLogs {

    ROOM_FOUND_BEFORE_UPLOAD_IMAGES("Room was found before upload images: {}"),
    ROOM_FOUND_BEFORE_UPLOAD_IMAGES_ASYNC("Room was found before upload images async: {}"),
    ROOM_FOUND_BEFORE_DELETE("Room was found before delete: {}"),
    ROOM_FOUND_BEFORE_DELETE_ASYNC("Room was found before delete async: {}"),
    ROOM_CREATED_OR_UPDATED("Room was created or updated: {}"),
    ROOM_CREATED_OR_UPDATED_ASYNC("Room was created or updated async: {}"),
    ROOM_FOUND_BY_ID("Room was found by id: {}"),
    ROOM_FOUND_BY_ID_ASYNC("Room was found by id async: {}"),
    ROOM_FOUND_BY_FILIAL_ID("Room was found by filial id: {}"),
    ROOM_FOUND_BY_FILIAL_ID_ASYNC("Room was found by filial id async: {}");

    private final String log;

    RoomLogs(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
