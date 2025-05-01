package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FlightSeatResponse;
import org.burgas.ticketservice.entity.FlightSeat;
import org.burgas.ticketservice.entity.OrderedTicket;
import org.burgas.ticketservice.entity.Ticket;
import org.burgas.ticketservice.exception.FlightSeatNotFoundException;
import org.burgas.ticketservice.exception.TicketAndFlightSeatMergeException;
import org.burgas.ticketservice.exception.TicketNotFoundException;
import org.burgas.ticketservice.mapper.FlightSeatMapper;
import org.burgas.ticketservice.repository.FlightSeatRepository;
import org.burgas.ticketservice.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.burgas.ticketservice.log.FlightSeatLogs.FLIGHT_SEAT_FOUND_BY_FLIGHT_ID;
import static org.burgas.ticketservice.message.FlightSeatMessages.FLIGHT_SEAT_NOT_FOUND;
import static org.burgas.ticketservice.message.FlightSeatMessages.TICKET_FLIGHT_SEAT_NOT_MERGED;
import static org.burgas.ticketservice.message.TicketMessages.TICKET_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FlightSeatService {

    private static final Logger log = LoggerFactory.getLogger(FlightSeatService.class);
    private final FlightSeatRepository flightSeatRepository;
    private final FlightSeatMapper flightSeatMapper;
    private final TicketRepository ticketRepository;

    public FlightSeatService(FlightSeatRepository flightSeatRepository, FlightSeatMapper flightSeatMapper, TicketRepository ticketRepository) {
        this.flightSeatRepository = flightSeatRepository;
        this.flightSeatMapper = flightSeatMapper;
        this.ticketRepository = ticketRepository;
    }

    public List<FlightSeatResponse> findAllByFlightId(final String flightId) {
        return this.flightSeatRepository.findFlightSeatsByFlightId(Long.parseLong(flightId))
                .stream()
                .peek(flightSeat -> log.info(FLIGHT_SEAT_FOUND_BY_FLIGHT_ID.getLogMessage(), flightSeat))
                .map(this.flightSeatMapper::toFlightSeatResponse)
                .collect(Collectors.toList());
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void reserveFlightSeat(final OrderedTicket orderedTicket) {
        Ticket ticket = this.ticketRepository.findById(orderedTicket.getTicketId())
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND.getMessage()));
        FlightSeat flightSeat = this.flightSeatRepository.findById(orderedTicket.getFlightSeatId())
                .orElseThrow(() -> new FlightSeatNotFoundException(FLIGHT_SEAT_NOT_FOUND.getLogMessage()));

        if (ticket.getCabinTypeId().equals(flightSeat.getCabinTypeId())) {
            flightSeat.setPurchased(true);
            this.flightSeatRepository.save(flightSeat);

        } else {
            throw new TicketAndFlightSeatMergeException(TICKET_FLIGHT_SEAT_NOT_MERGED.getLogMessage());
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void cancelReserveFlightSeat(final OrderedTicket orderedTicket) {
        FlightSeat flightSeat = this.flightSeatRepository.findById(orderedTicket.getFlightSeatId())
                .orElseThrow(() -> new FlightSeatNotFoundException(FLIGHT_SEAT_NOT_FOUND.getLogMessage()));
        flightSeat.setPurchased(false);
        this.flightSeatRepository.save(flightSeat);
    }
}
