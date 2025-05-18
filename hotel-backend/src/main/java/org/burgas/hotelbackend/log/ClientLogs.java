package org.burgas.hotelbackend.log;

public enum ClientLogs {

    CLIENT_FOUND_BEFORE_CREATE_OR_UPDATE("Client was found before create or update: {}"),
    CLIENT_FOUND_BEFORE_CREATE_OR_UPDATE_ASYNC("Client was found before create or update async: {}"),
    CLIENT_FOUND_BY_ID("Client was found by id: {}"),
    CLIENT_FOUND_BY_ID_ASYNC("Client was found by id async: {}"),
    CLIENT_FOUND_ALL("Client was found in list of all: {}"),
    CLIENT_FOUND_ALL_ASYNC("Client was found in list of all async: {}");

    private final String log;

    ClientLogs(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
