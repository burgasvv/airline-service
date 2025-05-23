package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.CountryRequest;
import org.burgas.hotelbackend.dto.CountryResponse;
import org.burgas.hotelbackend.entity.Country;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public final class CountryMapper implements MapperDataHandler<CountryRequest, Country, CountryResponse> {

    private final CountryRepository countryRepository;

    public CountryMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country toEntity(CountryRequest countryRequest) {
        Long countryId = this.getData(countryRequest.getId(), 0L);
        return this.countryRepository.findById(countryId)
                .map(
                        country -> Country.builder()
                                .id(country.getId())
                                .name(this.getData(countryRequest.getName(), country.getName()))
                                .description(this.getData(countryRequest.getDescription(), country.getDescription()))
                                .build()
                )
                .orElseGet(
                        () -> Country.builder()
                                .name(countryRequest.getName())
                                .description(countryRequest.getDescription())
                                .build()
                );
    }

    @Override
    public CountryResponse toResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .description(country.getDescription())
                .build();
    }
}
