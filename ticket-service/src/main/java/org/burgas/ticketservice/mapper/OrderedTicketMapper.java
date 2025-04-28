package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.*;
import org.burgas.ticketservice.entity.OrderedTicket;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.FlightSeatRepository;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.burgas.ticketservice.repository.OrderedTicketRepository;
import org.burgas.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Component;

@Component
public final class OrderedTicketMapper implements MapperDataHandler {

    private final OrderedTicketRepository orderedTicketRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final FlightSeatRepository flightSeatRepository;
    private final FlightSeatMapper flightSeatMapper;

    public OrderedTicketMapper(
            OrderedTicketRepository orderedTicketRepository, IdentityRepository identityRepository,
            IdentityMapper identityMapper, TicketRepository ticketRepository, TicketMapper ticketMapper,
            FlightSeatRepository flightSeatRepository, FlightSeatMapper flightSeatMapper
    ) {
        this.orderedTicketRepository = orderedTicketRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.flightSeatRepository = flightSeatRepository;
        this.flightSeatMapper = flightSeatMapper;
    }

    public OrderedTicket toOrderedTicket(final OrderedTicketRequest orderedTicketRequest) {
        Long orderedTicketId = this.getData(orderedTicketRequest.getId(), 0L);
        return this.orderedTicketRepository.findById(orderedTicketId)
                .map(
                        orderedTicket -> OrderedTicket.builder()
                                .id(orderedTicket.getId())
                                .identityId(this.getData(orderedTicketRequest.getIdentityId(), orderedTicket.getIdentityId()))
                                .ticketId(this.getData(orderedTicketRequest.getTicketId(), orderedTicket.getTicketId()))
                                .flightSeatId(this.getData(orderedTicketRequest.getFlightSeatId(), orderedTicket.getFlightSeatId()))
                                .closed(orderedTicket.getClosed())
                                .build()
                )
                .orElseGet(
                        () -> OrderedTicket.builder()
                                .identityId(orderedTicketRequest.getIdentityId())
                                .ticketId(orderedTicketRequest.getTicketId())
                                .flightSeatId(orderedTicketRequest.getFlightSeatId())
                                .closed(false)
                                .build()
                );
    }

    public OrderedTicketResponse toOrderedTicketResponse(final OrderedTicket orderedTicket) {
        return OrderedTicketResponse.builder()
                .id(orderedTicket.getId())
                .identity(
                        this.identityRepository.findById(orderedTicket.getIdentityId() == null ? 0L : orderedTicket.getIdentityId())
                                .map(this.identityMapper::toIdentityResponse)
                                .orElse(null)
                )
                .ticket(
                        this.ticketRepository.findById(orderedTicket.getTicketId())
                                .map(this.ticketMapper::toTicketResponse)
                                .orElse(null)
                )
                .flightSeat(
                        this.flightSeatRepository.findById(orderedTicket.getFlightSeatId())
                                .map(this.flightSeatMapper::toFlightSeatResponse)
                                .orElse(null)
                )
                .closed(orderedTicket.getClosed())
                .build();
    }
}
