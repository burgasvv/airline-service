package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.entity.Airport;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.AirportRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class AirportMapper {

    private final AirportRepository airportRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AirportMapper(AirportRepository airportRepository, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.airportRepository = airportRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    private <T> T getData(final T first, final T second) {
        return first == null || first == "" ? second : first;
    }

    public Mono<Airport> toAirport(final Mono<AirportRequest> airportRequestMono) {
        return airportRequestMono.flatMap(
                airportRequest -> {
                    Long airportId = this.getData(airportRequest.getId(), 0L);
                    return this.airportRepository.findById(airportId)
                            .flatMap(
                                    airport -> Mono.fromCallable(() ->
                                            Airport.builder()
                                                    .id(airport.getId())
                                                    .name(this.getData(airportRequest.getName(), airport.getName()))
                                                    .addressId(this.getData(airportRequest.getAddress().getId(), airport.getAddressId()))
                                                    .opened(this.getData(airportRequest.getOpened(), airport.getOpened()))
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            Airport.builder()
                                                    .name(airportRequest.getName())
                                                    .addressId(airportRequest.getAddress().getId())
                                                    .opened(airportRequest.getOpened())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<AirportResponse> toAirportResponse(final Mono<Airport> airportMono) {
        return airportMono.flatMap(
                airport -> this.addressRepository.findById(airport.getAddressId())
                        .flatMap(address -> this.addressMapper.toAddressResponse(Mono.fromCallable(() -> address)))
                        .flatMap(
                                addressResponse -> Mono.fromCallable(() ->
                                        AirportResponse.builder()
                                                .id(airport.getId())
                                                .name(airport.getName())
                                                .address(addressResponse)
                                                .opened(airport.getOpened())
                                                .build())
                        )
        );
    }
}
