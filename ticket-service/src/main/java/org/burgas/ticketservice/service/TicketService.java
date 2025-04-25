package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.TicketRequest;
import org.burgas.ticketservice.dto.TicketResponse;
import org.burgas.ticketservice.mapper.TicketMapper;
import org.burgas.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<TicketResponse> findAll() {
        return this.ticketRepository.findAll()
                .stream()
                .map(this.ticketMapper::toTicketResponse)
                .toList();
    }

    public TicketResponse findById(final String ticketId) {
        return this.ticketRepository.findById(Long.parseLong(ticketId))
                .map(this.ticketMapper::toTicketResponse)
                .orElseGet(TicketResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public TicketResponse createOrUpdate(final TicketRequest ticketRequest) {
        return Optional.of(this.ticketMapper.toTicket(ticketRequest))
                .map(this.ticketRepository::save)
                .map(this.ticketMapper::toTicketResponse)
                .orElseGet(TicketResponse::new);
    }
}
