package org.burgas.flightbackend.handler;

import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler {

    default <T> T getData(final T first, final T second) {
        return first == null || first == "" ? second : first;
    }
}
