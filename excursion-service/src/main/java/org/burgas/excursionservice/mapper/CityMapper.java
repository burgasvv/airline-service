package org.burgas.excursionservice.mapper;

import org.burgas.excursionservice.dto.CityRequest;
import org.burgas.excursionservice.dto.CityResponse;
import org.burgas.excursionservice.dto.CountryResponse;
import org.burgas.excursionservice.entity.City;
import org.burgas.excursionservice.handler.MapperDataHandler;
import org.burgas.excursionservice.repository.CityRepository;
import org.burgas.excursionservice.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper implements MapperDataHandler {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CityMapper(CityRepository cityRepository, CountryRepository countryRepository, CountryMapper countryMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public City toCity(final CityRequest cityRequest) {
        Long cityId = this.getData(cityRequest.getId(), 0L);
        return this.cityRepository.findById(cityId)
                .map(
                        city -> City.builder()
                                .id(city.getId())
                                .name(this.getData(cityRequest.getName(), city.getName()))
                                .description(this.getData(cityRequest.getDescription(), city.getDescription()))
                                .population(this.getData(cityRequest.getPopulation(), city.getPopulation()))
                                .countryId(this.getData(cityRequest.getCountryId(), city.getCountryId()))
                                .capital(this.getData(cityRequest.getCapital(), city.getCapital()))
                                .build()
                )
                .orElseGet(
                        () -> City.builder()
                                .name(cityRequest.getName())
                                .description(cityRequest.getDescription())
                                .population(cityRequest.getPopulation())
                                .countryId(cityRequest.getCountryId())
                                .capital(cityRequest.getCapital())
                                .build()
                );
    }

    public CityResponse toCityResponse(final City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .description(city.getDescription())
                .population(city.getPopulation())
                .country(
                        this.countryRepository.findById(city.getCountryId())
                                .map(this.countryMapper::toCountryResponse)
                                .orElseGet(CountryResponse::new)
                )
                .capital(city.getCapital())
                .build();
    }
}
