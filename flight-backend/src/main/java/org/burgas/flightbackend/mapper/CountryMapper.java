package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.CountryResponse;
import org.burgas.flightbackend.entity.Country;
import org.springframework.stereotype.Component;

@Component
public final class CountryMapper {

    public CountryResponse toCountryResponse(final Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}
