package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.CityResponse;
import org.burgas.ticketservice.dto.CountryResponse;
import org.burgas.ticketservice.entity.City;
import org.burgas.ticketservice.repository.CountryRepository;
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
