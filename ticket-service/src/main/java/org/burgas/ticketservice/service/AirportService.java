package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.exception.AirportNotCreatedException;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.mapper.AirportMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.ticketservice.log.AirportLogs.*;
import static org.burgas.ticketservice.message.AirportMessages.AIRPORT_NOT_CREATED;
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
                .map(this.airportMapper::toAirportResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findAllAsync() {
        return supplyAsync(this.airportRepository::findAll)
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_ALL_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toAirportResponse)
                                .collect(Collectors.toList())
                );
    }

    public List<AirportResponse> findByCountryId(final String countryId) {
        return this.airportRepository.findAirportsByCountryId(Long.valueOf(countryId))
                .stream()
                .peek(airport -> log.info(AIRPORT_FOUND_BY_COUNTRY_ID.getLogMessage(), airport))
                .map(this.airportMapper::toAirportResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findByCountryIdAsync(final String countryId) {
        return supplyAsync(() -> this.airportRepository.findAirportsByCountryId(Long.parseLong(countryId)))
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_BY_COUNTRY_ID_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toAirportResponse)
                                .collect(Collectors.toList())
                );
    }

    public List<AirportResponse> findByCityId(final String cityId) {
        return this.airportRepository.findAirportsByCityId(Long.valueOf(cityId))
                .stream()
                .peek(airport -> log.info(AIRPORT_FOUND_BY_CITY_ID.getLogMessage(), airport))
                .map(this.airportMapper::toAirportResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AirportResponse>> findByCityIdAsync(final String cityId) {
        return supplyAsync(() -> this.airportRepository.findAirportsByCityId(Long.parseLong(cityId)))
                .thenApplyAsync(
                        airports -> airports.stream()
                                .peek(airport -> log.info(AIRPORT_FOUND_BY_CITY_ID_ASYNC.getLogMessage(), airport))
                                .map(this.airportMapper::toAirportResponse)
                                .collect(Collectors.toList())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AirportResponse createOrUpdate(final AirportRequest airportRequest) {
        return of(this.addressMapper.toAddress(airportRequest.getAddress()))
                .map(this.addressRepository::save)
                .map(
                        address -> {
                            airportRequest.getAddress().setId(address.getId());
                            return this.airportMapper.toAirport(airportRequest);
                        }
                )
                .map(this.airportRepository::save)
                .map(this.airportMapper::toAirportResponse)
                .orElseThrow(() -> new AirportNotCreatedException(AIRPORT_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AirportResponse> createOrUpdateAsync(final AirportRequest airportRequest) {
        return supplyAsync(() -> this.addressMapper.toAddress(airportRequest.getAddress()))
                .thenApplyAsync(this.addressRepository::save)
                .thenApplyAsync(
                        address -> {
                            airportRequest.getAddress().setId(address.getId());
                            return this.airportMapper.toAirport(airportRequest);
                        }
                )
                .thenApplyAsync(this.airportRepository::save)
                .thenApplyAsync(this.airportMapper::toAirportResponse);
    }
}
