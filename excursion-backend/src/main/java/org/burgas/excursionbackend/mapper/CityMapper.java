package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.CityRequest;
import org.burgas.excursionbackend.dto.CityResponse;
import org.burgas.excursionbackend.dto.CountryResponse;
import org.burgas.excursionbackend.entity.City;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.CityRepository;
import org.burgas.excursionbackend.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper implements MapperDataHandler<CityRequest, City, CityResponse> {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CityMapper(CityRepository cityRepository, CountryRepository countryRepository, CountryMapper countryMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public City toEntity(CityRequest cityRequest) {
        Long cityId = this.getData(cityRequest.getId(), 0L);
        return this.cityRepository.findById(cityId)
                .map(
                        city -> City.builder()
                                .id(city.getId())
                                .name(this.getData(cityRequest.getName(), city.getName()))
                                .description(this.getData(cityRequest.getDescription(), city.getDescription()))
                                .population(this.getData(cityRequest.getPopulation(), city.getPopulation()))
                                .countryId(this.getData(cityRequest.getCountryId(), city.getCountryId()))
                                .capital(this.getData(cityRequest.getCapital(), city.getCapital()))
                                .build()
                )
                .orElseGet(
                        () -> City.builder()
                                .name(cityRequest.getName())
                                .description(cityRequest.getDescription())
                                .population(cityRequest.getPopulation())
                                .countryId(cityRequest.getCountryId())
                                .capital(cityRequest.getCapital())
                                .build()
                );
    }

    @Override
    public CityResponse toResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .description(city.getDescription())
                .population(city.getPopulation())
                .country(
                        this.countryRepository.findById(city.getCountryId())
                                .map(this.countryMapper::toResponse)
                                .orElseGet(CountryResponse::new)
                )
                .capital(city.getCapital())
                .imageId(city.getImageId())
                .build();
    }
}
