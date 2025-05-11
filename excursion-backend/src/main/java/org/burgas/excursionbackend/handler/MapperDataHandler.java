package org.burgas.excursionbackend.handler;

import org.burgas.excursionbackend.dto.Request;
import org.burgas.excursionbackend.dto.Response;
import org.burgas.excursionbackend.entity.AbstractEntity;
import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D getData(final D first, final D second) {
        return first == null || first == "" ? second : first;
    }

    S toEntity(T t);

    V toResponse(S s);
}
