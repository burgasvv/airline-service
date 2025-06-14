package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.AirportRequest;
import org.burgas.flightbackend.dto.AirportResponse;
import org.burgas.flightbackend.exception.AirportNotCreatedException;
import org.burgas.flightbackend.exception.AirportNotFoundException;
import org.burgas.flightbackend.mapper.AddressMapper;
import org.burgas.flightbackend.mapper.AirportMapper;
import org.burgas.flightbackend.repository.AddressRepository;
import org.burgas.flightbackend.repository.AirportRepository;
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
import static org.burgas.flightbackend.log.AirportLogs.*;
import static org.burgas.flightbackend.message.AirportMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AirportService {

    private static final Logger log = LoggerFactory.getLogger(AirportService.class);

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AirportService(
            AirportRepository airportRepository, AirportMapper airportMapper,
            AddressRepository addressRepository, AddressMapper addressMapper
    ) {
        this.airportRepository = airportRepository;
        this.airportMapper = airportMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public List<AirportResponse> findAll() {
        return this.airportRepository.findAll()
                .stream()
                .peek(airport -> log.info(AIRPORT_FOUND_ALL.getLogMessage(), airport))
                .map(this.airportMapper::toResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findAllAsync() {
        return supplyAsync(this.airportRepository::findAll)
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_ALL_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<AirportResponse> findAllPages(final Integer page, final Integer size) {
        return this.airportRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "name"))
                .map(this.airportMapper::toResponse);
    }

    public List<AirportResponse> findByCountryId(final String countryId) {
        return this.airportRepository.findAirportsByCountryId(Long.valueOf(countryId == null ? "0" : countryId))
                .stream()
                .peek(airport -> log.info(AIRPORT_FOUND_BY_COUNTRY_ID.getLogMessage(), airport))
                .map(this.airportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findByCountryIdAsync(final String countryId) {
        return supplyAsync(() -> this.airportRepository.findAirportsByCountryId(Long.parseLong(countryId == null ? "0" : countryId)))
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_BY_COUNTRY_ID_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public List<AirportResponse> findByCityId(final String cityId) {
        return this.airportRepository.findAirportsByCityId(Long.valueOf(cityId == null ? "0" : cityId))
                .stream()
                .peek(airport -> log.info(AIRPORT_FOUND_BY_CITY_ID.getLogMessage(), airport))
                .map(this.airportMapper::toResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findByCityIdAsync(final String cityId) {
        return supplyAsync(() -> this.airportRepository.findAirportsByCityId(Long.parseLong(cityId == null ? "0" : cityId)))
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_BY_CITY_ID_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AirportResponse createOrUpdate(final AirportRequest airportRequest) {
        return of(this.addressMapper.toEntity(airportRequest.getAddress()))
                .map(this.addressRepository::save)
                .map(
                        address -> {
                            airportRequest.getAddress().setId(address.getId());
                            return this.airportMapper.toEntity(airportRequest);
                        }
                )
                .map(this.airportRepository::save)
                .map(this.airportMapper::toResponse)
                .orElseThrow(() -> new AirportNotCreatedException(AIRPORT_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AirportResponse> createOrUpdateAsync(final AirportRequest airportRequest) {
        return supplyAsync(() -> this.addressMapper.toEntity(airportRequest.getAddress()))
                .thenApplyAsync(this.addressRepository::save)
                .thenApplyAsync(
                        address -> {
                            airportRequest.getAddress().setId(address.getId());
                            return this.airportMapper.toEntity(airportRequest);
                        }
                )
                .thenApplyAsync(this.airportRepository::save)
                .thenApplyAsync(this.airportMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String airportId) {
        return this.airportRepository.findById(Long.parseLong(airportId == null ? "0" : airportId))
                .map(
                        airport -> {
                            this.airportRepository.deleteById(airport.getId());
                            return AIRPORT_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new AirportNotFoundException(AIRPORT_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String airportId) {
        return supplyAsync(() -> this.airportRepository.findById(Long.parseLong(airportId == null ? "0" : airportId)))
                .thenApplyAsync(
                        airport -> airport.stream()
                                .peek(foundAirport -> log.info(AIRPORT_FOUND_BEFORE_DELETE.getLogMessage(), foundAirport))
                                .map(
                                        foundAirport -> {
                                            this.airportRepository.deleteById(foundAirport.getId());
                                            return AIRPORT_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new AirportNotFoundException(AIRPORT_NOT_FOUND.getMessage()))
                );
    }
}
