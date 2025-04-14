package org.burgas.excursionservice.service;

import org.burgas.excursionservice.dto.CountryRequest;
import org.burgas.excursionservice.dto.CountryResponse;
import org.burgas.excursionservice.exception.CountryNotFoundException;
import org.burgas.excursionservice.mapper.CountryMapper;
import org.burgas.excursionservice.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.log.CountryLogs.*;
import static org.burgas.excursionservice.message.CountryMessages.COUNTRY_DELETED;
import static org.burgas.excursionservice.message.CountryMessages.COUNTRY_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CountryService {

    private static final Logger log = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public List<CountryResponse> findAll() {
        return this.countryRepository.findAll()
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_ALL.getMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .toList();
    }

    @Async
    public CompletableFuture<List<CountryResponse>> findAllAsync() {
        return supplyAsync(this.countryRepository::findAll)
                .thenApplyAsync(
                        countries -> countries.stream()
                                .peek(country -> log.info(COUNTRY_FOUND_ALL_ASYNC.getMessage(), country))
                                .map(this.countryMapper::toCountryResponse)
                                .toList()
                );
    }

    public CountryResponse findById(final String countryId) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_BY_ID.getMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .findFirst()
                .orElseGet(CountryResponse::new);
    }

    @Async
    public CompletableFuture<CountryResponse> findByIdAsync(final String countryId) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.stream()
                                .peek(foundCountry -> log.info(COUNTRY_FOUND_BY_ID_ASYNC.getMessage(), foundCountry))
                                .map(this.countryMapper::toCountryResponse)
                                .findFirst()
                                .orElseGet(CountryResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CountryResponse createOrUpdate(final CountryRequest countryRequest) {
        return ofNullable(this.countryMapper.toCountry(countryRequest))
                .map(this.countryRepository::save)
                .map(this.countryMapper::toCountryResponse)
                .orElseGet(CountryResponse::new);
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CountryResponse> createOrUpdateAsync(final CountryRequest countryRequest) {
        return supplyAsync(() -> this.countryMapper.toCountry(countryRequest))
                .thenApplyAsync(this.countryRepository::save)
                .thenApplyAsync(this.countryMapper::toCountryResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String countryId) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .map(
                        country -> {
                            log.info(COUNTRY_FOUND_BEFORE_DELETING.getMessage(), country);
                            this.countryRepository.deleteById(country.getId());
                            return COUNTRY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()));
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String countryId) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.map(
                                foundCountry -> {
                                    log.info(COUNTRY_FOUND_BEFORE_DELETING.getMessage(), country);
                                    this.countryRepository.deleteById(foundCountry.getId());
                                    return COUNTRY_DELETED.getMessage();
                                }
                        )
                                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()))
                );
    }
}
