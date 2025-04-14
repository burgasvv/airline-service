package org.burgas.excursionservice.service;

import org.burgas.excursionservice.dto.CityRequest;
import org.burgas.excursionservice.dto.CityResponse;
import org.burgas.excursionservice.exception.CityNotFoundException;
import org.burgas.excursionservice.mapper.CityMapper;
import org.burgas.excursionservice.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.log.CityLogs.*;
import static org.burgas.excursionservice.message.CityMessages.CITY_DELETED;
import static org.burgas.excursionservice.message.CityMessages.CITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CityService {

    private static final Logger log = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public List<CityResponse> findAll() {
        return this.cityRepository.findAll()
                .stream()
                .peek(city -> log.info(CITY_FOUND_OF_ALL.getMessage(), city))
                .map(this.cityMapper::toCityResponse)
                .toList();
    }

    @Async
    public CompletableFuture<List<CityResponse>> findAllAsync() {
        return supplyAsync(this.cityRepository::findAll)
                .thenApplyAsync(
                        cities -> cities.stream()
                                .peek(city -> log.info(CITY_FOUND_OF_ALL_ASYNC.getMessage(), city))
                                .map(this.cityMapper::toCityResponse)
                                .toList()
                );
    }

    public CityResponse findById(final String cityId) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .stream()
                .peek(city -> log.info(CITY_FOUND_BY_ID.getMessage(), city))
                .map(this.cityMapper::toCityResponse)
                .findFirst()
                .orElseGet(CityResponse::new);
    }

    @Async
    public CompletableFuture<CityResponse> findByIdAsync(final String cityId) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_BY_ID_ASYNC.getMessage(), foundCity))
                                .map(this.cityMapper::toCityResponse)
                                .findFirst()
                                .orElseGet(CityResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CityResponse createOrUpdate(final CityRequest cityRequest) {
        return ofNullable(this.cityMapper.toCity(cityRequest))
                .map(this.cityRepository::save)
                .map(this.cityMapper::toCityResponse)
                .orElseGet(CityResponse::new);
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CityResponse> createOrUpdateAsync(final CityRequest cityRequest) {
        return supplyAsync(() -> this.cityMapper.toCity(cityRequest))
                .thenApplyAsync(this.cityRepository::save)
                .thenApplyAsync(this.cityMapper::toCityResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String cityId) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .map(
                        city -> {
                            log.info(CITY_FOUND_BEFORE_DELETING.getMessage(), city);
                            this.cityRepository.deleteById(city.getId());
                            return CITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()));
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String cityId) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.map(
                                foundCity -> {
                                    log.info(CITY_FOUND_BEFORE_DELETING_ASYNC.getMessage(), foundCity);
                                    this.cityRepository.deleteById(foundCity.getId());
                                    return CITY_DELETED.getMessage();
                                }
                        )
                                .orElseThrow(() -> new CityNotFoundException(CITY_DELETED.getMessage()))
                );
    }
}
