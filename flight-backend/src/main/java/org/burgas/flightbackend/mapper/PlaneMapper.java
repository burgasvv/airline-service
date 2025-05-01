package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.PlaneRequest;
import org.burgas.flightbackend.dto.PlaneResponse;
import org.burgas.flightbackend.entity.Plane;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.PlaneRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class PlaneMapper implements MapperDataHandler {

    private final PlaneRepository planeRepository;

    public PlaneMapper(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public Plane toPlane(final PlaneRequest planeRequest) {
        Long planeId = this.getData(planeRequest.getId(), 0L);
        return this.planeRepository.findById(planeId)
                .map(
                        plane -> Plane.builder()
                                .id(plane.getId())
                                .number(plane.getNumber())
                                .model(this.getData(planeRequest.getModel(), plane.getModel()))
                                .businessClass(this.getData(planeRequest.getBusinessClass(), plane.getBusinessClass()))
                                .economyClass(this.getData(planeRequest.getEconomyClass(), plane.getEconomyClass()))
                                .free(this.getData(planeRequest.getFree(), plane.getFree()))
                                .build()
                )
                .orElseGet(
                        () -> Plane.builder()
                                .number(
                                        UUID.randomUUID().toString()
                                                .replaceAll("-","")
                                                .substring(1,10)
                                )
                                .model(planeRequest.getModel())
                                .businessClass(planeRequest.getBusinessClass())
                                .economyClass(planeRequest.getEconomyClass())
                                .free(planeRequest.getFree())
                                .build()
                );
    }

    public PlaneResponse toPlaneResponse(final Plane plane) {
        return PlaneResponse.builder()
                .id(plane.getId())
                .number(plane.getNumber())
                .model(plane.getModel())
                .businessClass(plane.getBusinessClass())
                .economyClass(plane.getEconomyClass())
                .free(plane.getFree())
                .build();
    }
}
