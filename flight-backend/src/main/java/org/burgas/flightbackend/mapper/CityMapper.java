package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.CityResponse;
import org.burgas.flightbackend.dto.CountryResponse;
import org.burgas.flightbackend.entity.City;
import org.burgas.flightbackend.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CityMapper(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public CityResponse toCityResponse(final City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .country(
                        this.countryRepository.findById(city.getCountryId())
                                .map(this.countryMapper::toCountryResponse)
                                .orElseGet(CountryResponse::new)
                )
                .build();
    }
}
