package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.mapper.AirportMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.of;
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

    public List<AirportResponse> findAll() {
        return this.airportRepository.findAll()
                .stream()
                .map(this.airportMapper::toAirportResponse)
                .toList();
    }

    public List<AirportResponse> findByCountryId(final String countryId) {
        return this.airportRepository.findAirportsByCountryId(Long.valueOf(countryId))
                .stream()
                .map(this.airportMapper::toAirportResponse)
                .toList();
    }

    public List<AirportResponse> findByCityId(final String cityId) {
        return this.airportRepository.findAirportsByCityId(Long.valueOf(cityId))
                .stream()
                .map(this.airportMapper::toAirportResponse)
                .toList();
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
                .orElseGet(AirportResponse::new);
    }
}
