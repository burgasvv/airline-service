package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.CityResponse;
import org.burgas.excursionbackend.dto.SightRequest;
import org.burgas.excursionbackend.dto.SightResponse;
import org.burgas.excursionbackend.entity.Sight;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.CityRepository;
import org.burgas.excursionbackend.repository.SightRepository;
import org.springframework.stereotype.Component;

@Component
public final class SightMapper implements MapperDataHandler {

    private final SightRepository sightRepository;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public SightMapper(SightRepository sightRepository, CityRepository cityRepository, CityMapper cityMapper) {
        this.sightRepository = sightRepository;
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public Sight toSight(final SightRequest sightRequest) {
        Long sightId = this.getData(sightRequest.getId(), 0L);
        return this.sightRepository.findById(sightId)
                .map(
                        sight -> Sight.builder()
                                .id(sight.getId())
                                .name(this.getData(sightRequest.getName(), sight.getName()))
                                .description(this.getData(sightRequest.getDescription(), sight.getDescription()))
                                .cityId(this.getData(sightRequest.getCityId(), sight.getCityId()))
                                .build()
                )
                .orElseGet(
                        () -> Sight.builder()
                                .name(sightRequest.getName())
                                .description(sightRequest.getDescription())
                                .cityId(sightRequest.getCityId())
                                .build()
                );
    }

    public SightResponse toSightResponse(final Sight sight) {
        return SightResponse.builder()
                .id(sight.getId())
                .name(sight.getName())
                .description(sight.getDescription())
                .city(
                        this.cityRepository.findById(sight.getCityId())
                                .map(this.cityMapper::toCityResponse)
                                .orElseGet(CityResponse::new)
                )
                .imageId(sight.getImageId())
                .build();
    }
}
