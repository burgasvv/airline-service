package org.burgas.hotelbackend.handler;

import org.springframework.stereotype.Component;

@Component
public interface MapperDataHandler<Request, Entity, Response> {

    default <Data> Data getData(Data first, Data second) {
        return first == null || first == "" ? second : first;
    }

    Entity toEntity(Request request);

    Response toResponse(Entity entity);
}
