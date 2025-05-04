package org.burgas.hotelbackend.handler;

import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler {

    default <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }
}
