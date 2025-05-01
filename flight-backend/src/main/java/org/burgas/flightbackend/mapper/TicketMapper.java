package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.FlightResponse;
import org.burgas.flightbackend.dto.TicketRequest;
import org.burgas.flightbackend.dto.TicketResponse;
import org.burgas.flightbackend.entity.CabinType;
import org.burgas.flightbackend.entity.Ticket;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.CabinTypeRepository;
import org.burgas.flightbackend.repository.FlightRepository;
import org.burgas.flightbackend.repository.TicketRepository;
import org.springframework.stereotype.Component;

@Component
public final class TicketMapper implements MapperDataHandler {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final CabinTypeRepository cabinTypeRepository;

    public TicketMapper(
            TicketRepository ticketRepository, FlightRepository flightRepository,
            FlightMapper flightMapper, CabinTypeRepository cabinTypeRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.cabinTypeRepository = cabinTypeRepository;
    }

    public Ticket toTicket(final TicketRequest ticketRequest) {
        Long ticketId = this.getData(ticketRequest.getId(), 0L);
        return this.ticketRepository.findById(ticketId)
                .map(
                        ticket -> Ticket.builder()
                                .id(ticket.getId())
                                .flightId(this.getData(ticketRequest.getFlightId(), ticket.getFlightId()))
                                .cabinTypeId(this.getData(ticketRequest.getCabinTypeId(), ticket.getCabinTypeId()))
                                .amount(this.getData(ticketRequest.getAmount(), ticket.getAmount()))
                                .price(this.getData(ticketRequest.getPrice(), ticket.getAmount()))
                                .closed(this.getData(ticketRequest.getClosed(), ticket.getClosed()))
                                .build()
                )
                .orElseGet(
                        () -> Ticket.builder()
                                .flightId(ticketRequest.getFlightId())
                                .cabinTypeId(ticketRequest.getCabinTypeId())
                                .price(ticketRequest.getPrice())
                                .amount(ticketRequest.getAmount())
                                .closed(ticketRequest.getClosed())
                                .build()
                );
    }

    public TicketResponse toTicketResponse(final Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .flight(
                        this.flightRepository.findById(ticket.getFlightId())
                                .map(this.flightMapper::toFlightResponse)
                                .orElseGet(FlightResponse::new)
                )
                .cabinType(
                        this.cabinTypeRepository.findById(ticket.getCabinTypeId())
                                .orElseGet(CabinType::new)
                )
                .amount(ticket.getAmount())
                .price(ticket.getPrice())
                .closed(ticket.getClosed())
                .build();
    }
}
