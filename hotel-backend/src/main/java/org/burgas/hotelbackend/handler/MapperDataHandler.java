package org.burgas.hotelbackend.handler;

import org.burgas.hotelbackend.dto.Request;
import org.burgas.hotelbackend.dto.Response;
import org.burgas.hotelbackend.entity.AbstractEntity;
import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D getData(D first, D second) {
        return first == null || first == "" ? second : first;
    }

    S toEntity(T t);

    V toResponse(S s);
}
