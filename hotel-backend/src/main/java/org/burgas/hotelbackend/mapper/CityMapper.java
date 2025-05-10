package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.CityRequest;
import org.burgas.hotelbackend.dto.CityResponse;
import org.burgas.hotelbackend.entity.City;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.CityRepository;
import org.burgas.hotelbackend.repository.CountryRepository;
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
                                .countryId(this.getData(cityRequest.getCountryId(), city.getCountryId()))
                                .capital(this.getData(cityRequest.getCapital(), city.getCapital()))
                                .build()
                )
                .orElseGet(
                        () -> City.builder()
                                .name(cityRequest.getName())
                                .description(cityRequest.getDescription())
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
                .country(
                        this.countryRepository.findById(city.getCountryId())
                                .map(this.countryMapper::toResponse)
                                .orElse(null)
                )
                .capital(city.getCapital())
                .build();
    }
}
