package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.TicketRequest;
import org.burgas.flightbackend.dto.TicketResponse;
import org.burgas.flightbackend.exception.TicketNotCreatedException;
import org.burgas.flightbackend.log.TicketLogs;
import org.burgas.flightbackend.mapper.TicketMapper;
import org.burgas.flightbackend.message.TicketMessages;
import org.burgas.flightbackend.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.burgas.flightbackend.log.TicketLogs.TICKET_FOUND_ALL;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<TicketResponse> findAll() {
        return this.ticketRepository.findAll()
                .stream()
                .peek(ticket -> log.info(TICKET_FOUND_ALL.getLogMessage(), ticket))
                .map(this.ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    public List<TicketResponse> findAllByFlightId(final String flightId) {
        return this.ticketRepository.findTicketsByFlightId(Long.parseLong(flightId))
                .stream()
                .peek(ticket -> log.info(TicketLogs.TICKET_FOUND_BY_FLIGHT_ID.getLogMessage(), ticket))
                .map(this.ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse findById(final String ticketId) {
        return this.ticketRepository.findById(Long.parseLong(ticketId))
                .stream()
                .peek(ticket -> log.info(TicketLogs.TICKET_WAS_FOUND_BY_ID.getLogMessage(), ticket))
                .map(this.ticketMapper::toTicketResponse)
                .findFirst()
                .orElseGet(TicketResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public TicketResponse createOrUpdate(final TicketRequest ticketRequest) {
        return of(this.ticketMapper.toTicket(ticketRequest))
                .map(this.ticketRepository::save)
                .map(this.ticketMapper::toTicketResponse)
                .orElseThrow(
                        () -> new TicketNotCreatedException(TicketMessages.TICKET_NOT_CREATED.getMessage())
                );
    }
}
