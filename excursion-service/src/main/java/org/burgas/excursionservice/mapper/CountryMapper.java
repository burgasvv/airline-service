package org.burgas.excursionservice.mapper;

import org.burgas.excursionservice.dto.CountryRequest;
import org.burgas.excursionservice.dto.CountryResponse;
import org.burgas.excursionservice.entity.Country;
import org.burgas.excursionservice.handler.MapperDataHandler;
import org.burgas.excursionservice.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public final class CountryMapper implements MapperDataHandler {

    private final CountryRepository countryRepository;

    public CountryMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country toCountry(final CountryRequest countryRequest) {
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

    public CountryResponse toCountryResponse(final Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .description(country.getDescription())
                .population(country.getPopulation())
                .imageId(country.getImageId())
                .build();
    }
}
