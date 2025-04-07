package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.CountryResponse;
import org.burgas.ticketservice.entity.Country;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class CountryMapper {

    public Mono<CountryResponse> toCountryResponse(final Mono<Country> countryMono) {
        return countryMono.flatMap(
                country -> Mono.fromCallable(
                        () -> CountryResponse.builder()
                                .id(country.getId())
                                .name(country.getName())
                                .build()
                )
        );
    }
}
