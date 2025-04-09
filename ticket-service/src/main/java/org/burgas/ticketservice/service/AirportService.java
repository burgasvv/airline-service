package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.mapper.AirportMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AirportService {

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

    public Flux<AirportResponse> findAll() {
        return this.airportRepository.findAll()
                .flatMap(airport -> this.airportMapper.toAirportResponse(Mono.fromCallable(() -> airport)));
    }

    public Flux<AirportResponse> findByCountryId(final String countryId) {
        return this.airportRepository.findAirportsByCountryId(Long.valueOf(countryId))
                .flatMap(airport -> this.airportMapper.toAirportResponse(Mono.fromCallable(() -> airport)));
    }

    public Flux<AirportResponse> findByCityId(final String cityId) {
        return this.airportRepository.findAirportsByCityId(Long.valueOf(cityId))
                .flatMap(airport -> this.airportMapper.toAirportResponse(Mono.fromCallable(() -> airport)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<AirportResponse> createOrUpdate(final Mono<AirportRequest> airportRequestMono) {
        return airportRequestMono.flatMap(
                airportRequest -> this.addressMapper.toAddress(Mono.fromCallable(airportRequest::getAddress))
                        .flatMap(this.addressRepository::save)
                        .flatMap(
                                address -> {
                                    airportRequest.getAddress().setId(address.getId());
                                    return this.airportMapper.toAirport(Mono.fromCallable(() -> airportRequest));
                                }
                        )
                        .flatMap(this.airportRepository::save)
                        .flatMap(airport -> this.airportMapper.toAirportResponse(Mono.fromCallable(() -> airport)))
        );
    }
}
