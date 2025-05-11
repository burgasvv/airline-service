package org.burgas.flightbackend.handler;

import org.burgas.flightbackend.dto.Request;
import org.burgas.flightbackend.dto.Response;
import org.burgas.flightbackend.entity.AbstractEntity;
import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D getData(final D first, final D second) {
        return first == null || first == "" ? second : first;
    }

    S toEntity(T t);

    V toResponse(S s);
}
