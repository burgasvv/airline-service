package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FlightSeatResponse;
import org.burgas.ticketservice.mapper.FlightSeatMapper;
import org.burgas.ticketservice.repository.FlightSeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.burgas.ticketservice.log.FlightSeatLogs.FLIGHT_SEAT_FOUND_BY_FLIGHT_ID;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FlightSeatService {

    private static final Logger log = LoggerFactory.getLogger(FlightSeatService.class);
    private final FlightSeatRepository flightSeatRepository;
    private final FlightSeatMapper flightSeatMapper;

    public FlightSeatService(FlightSeatRepository flightSeatRepository, FlightSeatMapper flightSeatMapper) {
        this.flightSeatRepository = flightSeatRepository;
        this.flightSeatMapper = flightSeatMapper;
    }

    public List<FlightSeatResponse> findAllByFlightId(final String flightId) {
        return this.flightSeatRepository.findFlightSeatsByFlightId(Long.parseLong(flightId))
                .stream()
                .peek(flightSeat -> log.info(FLIGHT_SEAT_FOUND_BY_FLIGHT_ID.getLogMessage(), flightSeat))
                .map(this.flightSeatMapper::toFlightSeatResponse)
                .collect(Collectors.toList());
    }
}
