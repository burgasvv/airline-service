package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.FlightResponse;
import org.burgas.ticketservice.dto.TicketRequest;
import org.burgas.ticketservice.dto.TicketResponse;
import org.burgas.ticketservice.entity.CabinType;
import org.burgas.ticketservice.entity.Ticket;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.CabinTypeRepository;
import org.burgas.ticketservice.repository.FlightRepository;
import org.burgas.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper implements MapperDataHandler {

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
                                .build()
                )
                .orElseGet(
                        () -> Ticket.builder()
                                .flightId(ticketRequest.getFlightId())
                                .cabinTypeId(ticketRequest.getCabinTypeId())
                                .price(ticketRequest.getPrice())
                                .amount(ticketRequest.getAmount())
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
                .build();
    }
}
