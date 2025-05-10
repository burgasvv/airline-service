package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.CountryRequest;
import org.burgas.excursionbackend.dto.CountryResponse;
import org.burgas.excursionbackend.entity.Country;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.CountryRepository;
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
                                .population(this.getData(countryRequest.getPopulation(), country.getPopulation()))
                                .build()
                )
                .orElseGet(
                        () -> Country.builder()
                                .name(countryRequest.getName())
                                .description(countryRequest.getDescription())
                                .population(countryRequest.getPopulation())
                                .build()
                );
    }

    @Override
    public CountryResponse toResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .description(country.getDescription())
                .population(country.getPopulation())
                .imageId(country.getImageId())
                .build();
    }
}
