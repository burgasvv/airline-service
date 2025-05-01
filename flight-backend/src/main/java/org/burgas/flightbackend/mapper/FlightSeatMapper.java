package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.FlightSeatResponse;
import org.burgas.flightbackend.entity.CabinType;
import org.burgas.flightbackend.entity.Flight;
import org.burgas.flightbackend.entity.FlightSeat;
import org.burgas.flightbackend.repository.CabinTypeRepository;
import org.burgas.flightbackend.repository.FlightRepository;
import org.springframework.stereotype.Component;

@Component
public final class FlightSeatMapper {

    private final CabinTypeRepository cabinTypeRepository;
    private final FlightRepository flightRepository;

    public FlightSeatMapper(CabinTypeRepository cabinTypeRepository, FlightRepository flightRepository) {
        this.cabinTypeRepository = cabinTypeRepository;
        this.flightRepository = flightRepository;
    }

    public FlightSeatResponse toFlightSeatResponse(final FlightSeat flightSeat) {
        return FlightSeatResponse.builder()
                .id(flightSeat.getId())
                .number(flightSeat.getNumber())
                .cabinType(this.cabinTypeRepository.findById(flightSeat.getCabinTypeId()).orElseGet(CabinType::new))
                .flightNumber(this.flightRepository.findById(flightSeat.getFlightId()).orElseGet(Flight::new).getNumber())
                .purchased(flightSeat.getPurchased())
                .closed(flightSeat.getClosed())
                .build();
    }
}
