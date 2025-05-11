package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.AddressResponse;
import org.burgas.flightbackend.dto.AirportRequest;
import org.burgas.flightbackend.dto.AirportResponse;
import org.burgas.flightbackend.entity.Airport;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.AddressRepository;
import org.burgas.flightbackend.repository.AirportRepository;
import org.springframework.stereotype.Component;

@Component
public final class AirportMapper implements MapperDataHandler<AirportRequest, Airport, AirportResponse> {

    private final AirportRepository airportRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AirportMapper(AirportRepository airportRepository, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.airportRepository = airportRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public Airport toEntity(AirportRequest airportRequest) {
        Long airportId = this.getData(airportRequest.getId(), 0L);
        return this.airportRepository.findById(airportId)
                .map(
                        airport -> Airport.builder()
                                .id(airport.getId())
                                .name(this.getData(airportRequest.getName(), airport.getName()))
                                .addressId(this.getData(airportRequest.getAddress().getId(), airport.getAddressId()))
                                .opened(this.getData(airportRequest.getOpened(), airport.getOpened()))
                                .build()
                )
                .orElseGet(
                        () -> Airport.builder()
                                .name(airportRequest.getName())
                                .addressId(airportRequest.getAddress().getId())
                                .opened(airportRequest.getOpened())
                                .build()
                );
    }

    @Override
    public AirportResponse toResponse(Airport airport) {
        return AirportResponse.builder()
                .id(airport.getId())
                .name(airport.getName())
                .address(
                        this.addressRepository.findById(airport.getAddressId())
                                .map(this.addressMapper::toResponse)
                                .orElseGet(AddressResponse::new)
                )
                .opened(airport.getOpened())
                .build();
    }
}
