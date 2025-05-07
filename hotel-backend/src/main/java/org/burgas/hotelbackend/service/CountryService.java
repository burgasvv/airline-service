package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.CountryRequest;
import org.burgas.hotelbackend.dto.CountryResponse;
import org.burgas.hotelbackend.exception.CountryNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.exception.CountryNotFoundException;
import org.burgas.hotelbackend.mapper.CountryMapper;
import org.burgas.hotelbackend.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.CountryLogs.*;
import static org.burgas.hotelbackend.message.CountryMessages.*;
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
                .peek(country -> log.info(COUNTRY_FOUND_ALL.getLogMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<CountryResponse>> findAllAsync() {
        return supplyAsync(this.countryRepository::findAll)
                .thenApplyAsync(
                        countries -> countries.stream()
                                .peek(country -> log.info(COUNTRY_FOUND_ALL_ASYNC.getLogMessage(), country))
                                .map(this.countryMapper::toCountryResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<CountryResponse> findAll(final Integer page, final Integer size) {
        return this.countryRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "name"))
                .map(this.countryMapper::toCountryResponse);
    }

    public CountryResponse findById(final Long countryId) {
        return this.countryRepository.findById(countryId)
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_BY_ID_ASYNC.getLogMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .findFirst()
                .orElseGet(CountryResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CountryResponse> findByIdAsync(final Long countryId) {
        return supplyAsync(() -> this.countryRepository.findById(countryId))
                .thenApplyAsync(
                        country -> country.stream()
                                .peek(foundCountry -> log.info(COUNTRY_FOUND_BY_ID_ASYNC.getLogMessage(), foundCountry))
                                .map(this.countryMapper::toCountryResponse)
                                .findFirst()
                                .orElseGet(CountryResponse::new)
                );
    }

    public CountryResponse findByName(final String name) {
        return this.countryRepository.findCountryByName(name)
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_BY_NAME.getLogMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .findFirst()
                .orElseGet(CountryResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CountryResponse> findByNameAsync(final String name) {
        return supplyAsync(() -> this.countryRepository.findCountryByName(name))
                .thenApplyAsync(
                        country -> country.stream()
                                .peek(foundCountry -> log.info(COUNTRY_FOUND_BY_NAME_ASYNC.getLogMessage(), foundCountry))
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
        return of(this.countryMapper.toCountry(countryRequest))
                .map(this.countryRepository::save)
                .stream()
                .peek(createdOrUpdatedCountry -> log.info(COUNTRY_CREATED_OR_UPDATED.getLogMessage(), createdOrUpdatedCountry))
                .map(this.countryMapper::toCountryResponse)
                .findFirst()
                .orElseThrow(
                        () -> new CountryNotCreatedOrUpdatedException(COUNTRY_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CountryResponse> createOrUpdateAsync(final CountryRequest countryRequest) {
        return supplyAsync(() -> this.countryMapper.toCountry(countryRequest))
                .thenApplyAsync(this.countryRepository::save)
                .thenApplyAsync(
                        country -> of(country).stream()
                                .peek(createOrdUpdatedCountry -> log.info(COUNTRY_CREATED_OR_UPDATED_ASYNC.getLogMessage(), createOrdUpdatedCountry))
                                .findFirst()
                )
                .thenApplyAsync(
                        country -> country
                                .map(this.countryMapper::toCountryResponse)
                                .orElseThrow(
                                        () -> new CountryNotCreatedOrUpdatedException(COUNTRY_NOT_CREATED_OR_UPDATED_ASYNC.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long countryId) {
        return this.countryRepository.findById(countryId)
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_BEFORE_DELETE.getLogMessage(), country))
                .map(
                        country -> {
                            this.countryRepository.deleteById(country.getId());
                            return COUNTRY_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long countryId) {
        return supplyAsync(() -> this.countryRepository.findById(countryId))
                .thenApplyAsync(
                        country -> country.stream()
                                .peek(foundCountry -> log.info(COUNTRY_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundCountry))
                                .map(
                                        foundCountry -> {
                                            this.countryRepository.deleteById(foundCountry.getId());
                                            return COUNTRY_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage())
                                )
                );
    }
}
