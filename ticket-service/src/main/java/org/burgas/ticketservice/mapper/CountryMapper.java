package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.CountryResponse;
import org.burgas.ticketservice.entity.Country;
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
