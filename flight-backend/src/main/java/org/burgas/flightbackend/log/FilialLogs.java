package org.burgas.flightbackend.log;

public enum FilialLogs {

    FILIAL_FOUND_BEFORE_DELETE("Filial was found before delete: {}"),
    FILIAL_FOUND_ALL("Filial was found in list of all: {}"),
    FILIAL_FOUND_ALL_ASYNC("Filial was found in list of all async: {}"),
    FILIAL_FOUND_BY_COUNTRY_ID("Filial was found by country id: {}"),
    FILIAL_FOUND_BY_COUNTRY_ID_ASYNC("Filial was found by country id async: {}"),
    FILIAL_FOUND_BY_CITY_ID("Filial was found by city id: {}"),
    FILIAL_FOUND_BY_CITY_ID_ASYNC("Filial was found by city id async: {}"),
    FILIAL_FOUND_BY_ID("Filial was found by id: {}");

    private final String logMessage;

    FilialLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
