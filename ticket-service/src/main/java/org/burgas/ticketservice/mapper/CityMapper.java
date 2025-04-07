package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.CityResponse;
import org.burgas.ticketservice.entity.City;
import org.burgas.ticketservice.repository.CountryRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class CityMapper {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CityMapper(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public Mono<CityResponse> toCityResponse(final Mono<City> cityMono) {
        return cityMono.flatMap(
                city -> this.countryRepository.findById(city.getCountryId())
                        .flatMap(country -> this.countryMapper.toCountryResponse(
                                Mono.fromCallable(() -> country)
                        ))
                        .flatMap(
                                countryResponse -> Mono.fromCallable(
                                        () -> CityResponse.builder()
                                                .id(city.getId())
                                                .name(city.getName())
                                                .country(countryResponse)
                                                .build()
                                )
                        )
        );
    }
}
