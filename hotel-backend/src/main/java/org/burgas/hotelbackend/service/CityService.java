package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.CityRequest;
import org.burgas.hotelbackend.dto.CityResponse;
import org.burgas.hotelbackend.exception.CityNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.exception.CityNotFoundException;
import org.burgas.hotelbackend.mapper.CityMapper;
import org.burgas.hotelbackend.repository.CityRepository;
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
import static org.burgas.hotelbackend.log.CityLogs.*;
import static org.burgas.hotelbackend.message.CityMessages.*;
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
                .peek(city -> log.info(CITY_FOUND_ALL.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Page<CityResponse> findAllPages(final Integer page, final Integer size) {
        return this.cityRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name"))
                .map(this.cityMapper::toResponse);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<CityResponse>> findAllAsync() {
        return supplyAsync(this.cityRepository::findAll)
                .thenApplyAsync(
                        cities -> cities.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_ALL_ASYNC.getLogMessage(), foundCity))
                                .map(this.cityMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public CityResponse findById(final Long cityId) {
        return this.cityRepository.findById(cityId == null ? 0L : cityId)
                .stream()
                .peek(city -> log.info(CITY_FOUND_BY_ID.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .findFirst()
                .orElseGet(CityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CityResponse> findByIdAsync(final Long cityId) {
        return supplyAsync(() -> this.cityRepository.findById(cityId == null ? 0L : cityId))
                .thenApplyAsync(
                        city -> city.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_BY_ID_ASYNC.getLogMessage(), foundCity))
                                .map(this.cityMapper::toResponse)
                                .findFirst()
                                .orElseGet(CityResponse::new)
                );
    }

    public CityResponse findByName(final String name) {
        return this.cityRepository.findCityByName(name == null ? "" : name)
                .stream()
                .peek(city -> log.info(CITY_FOUND_BY_NAME.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .findFirst()
                .orElseGet(CityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CityResponse> findByNameAsync(final String name) {
        return supplyAsync(() -> this.cityRepository.findCityByName(name == null ? "" : name))
                .thenApplyAsync(
                        city -> city.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_BY_NAME_ASYNC.getLogMessage(), foundCity))
                                .map(this.cityMapper::toResponse)
                                .findFirst()
                                .orElseGet(CityResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CityResponse createOrUpdate(final CityRequest cityRequest) {
        return of(this.cityMapper.toEntity(cityRequest))
                .map(this.cityRepository::save)
                .stream()
                .peek(city -> log.info(CITY_CREATED_OR_UPDATED.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new CityNotCreatedOrUpdatedException(CITY_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CityResponse> createOrUpdateAsync(final CityRequest cityRequest) {
        return supplyAsync(() -> this.cityMapper.toEntity(cityRequest))
                .thenApplyAsync(this.cityRepository::save)
                .thenApplyAsync(
                        city -> of(city).stream()
                                .peek(createdOrUpdatedCity -> log.info(CITY_CREATED_OR_UPDATED_ASYNC.getLogMessage(), createdOrUpdatedCity))
                                .map(this.cityMapper::toResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new CityNotCreatedOrUpdatedException(CITY_NOT_CREATED_OR_UPDATED.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long cityId) {
        return this.cityRepository.findById(cityId == null ? 0L : cityId)
                .stream()
                .peek(city -> log.info(CITY_FOUND_BEFORE_DELETE.getLogMessage(), city))
                .map(
                        city -> {
                            this.cityRepository.deleteById(city.getId());
                            return CITY_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new CityNotFoundException(CITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long cityId) {
        return supplyAsync(() -> this.cityRepository.findById(cityId == null ? 0L : cityId))
                .thenApplyAsync(
                        city -> city.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundCity))
                                .map(
                                        foundCity -> {
                                            this.cityRepository.deleteById(foundCity.getId());
                                            return CITY_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new CityNotFoundException(CITY_NOT_FOUND.getMessage())
                                )
                );
    }
}
