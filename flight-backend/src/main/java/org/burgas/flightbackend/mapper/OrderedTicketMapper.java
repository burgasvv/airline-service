package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.*;
import org.burgas.flightbackend.entity.OrderedTicket;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.FlightSeatRepository;
import org.burgas.flightbackend.repository.IdentityRepository;
import org.burgas.flightbackend.repository.OrderedTicketRepository;
import org.burgas.flightbackend.repository.TicketRepository;
import org.springframework.stereotype.Component;

@Component
public final class OrderedTicketMapper implements MapperDataHandler<OrderedTicketRequest, OrderedTicket, OrderedTicketResponse> {

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

    @Override
    public OrderedTicket toEntity(OrderedTicketRequest orderedTicketRequest) {
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

    @Override
    public OrderedTicketResponse toResponse(OrderedTicket orderedTicket) {
        return OrderedTicketResponse.builder()
                .id(orderedTicket.getId())
                .identity(
                        this.identityRepository.findById(orderedTicket.getIdentityId() == null ? 0L : orderedTicket.getIdentityId())
                                .map(this.identityMapper::toResponse)
                                .orElse(null)
                )
                .ticket(
                        this.ticketRepository.findById(orderedTicket.getTicketId())
                                .map(this.ticketMapper::toResponse)
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
