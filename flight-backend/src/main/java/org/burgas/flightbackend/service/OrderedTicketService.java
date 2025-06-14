package org.burgas.flightbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.burgas.flightbackend.dto.OrderedTicketRequest;
import org.burgas.flightbackend.dto.OrderedTicketResponse;
import org.burgas.flightbackend.entity.OrderedTicket;
import org.burgas.flightbackend.exception.OrderedTicketNotFoundException;
import org.burgas.flightbackend.mapper.OrderedTicketMapper;
import org.burgas.flightbackend.repository.OrderedTicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.burgas.flightbackend.log.OrderedTicketLogs.*;
import static org.burgas.flightbackend.message.OrderedTicketMessages.ORDERED_TICKET_CANCELLED;
import static org.burgas.flightbackend.message.OrderedTicketMessages.ORDERED_TICKET_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class OrderedTicketService {

    private static final Logger log = LoggerFactory.getLogger(OrderedTicketService.class);

    private final OrderedTicketRepository orderedTicketRepository;
    private final OrderedTicketMapper orderedTicketMapper;
    private final FlightSeatService flightSeatService;

    public OrderedTicketService(
            OrderedTicketRepository orderedTicketRepository,
            OrderedTicketMapper orderedTicketMapper, FlightSeatService flightSeatService
    ) {
        this.orderedTicketRepository = orderedTicketRepository;
        this.orderedTicketMapper = orderedTicketMapper;
        this.flightSeatService = flightSeatService;
    }

    public List<OrderedTicketResponse> findAll() {
        return this.orderedTicketRepository.findAll()
                .stream()
                .peek(orderedTicket -> log.info(ORDERED_TICKET_FOUND_ALL.getLogMessage(), orderedTicket))
                .map(this.orderedTicketMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderedTicketResponse> findAllByIdentityId(final String identityId) {
        return this.orderedTicketRepository.findOrderedTicketsByIdentityId(Long.parseLong(identityId == null ? "0" : identityId))
                .stream()
                .peek(orderedTicket -> log.info(ORDERED_TICKET_FOUND_BY_IDENTITY_ID.getLogMessage(), orderedTicket))
                .map(this.orderedTicketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<OrderedTicketResponse> findAllInSession(final HttpServletRequest httpServletRequest) {
        return ofNullable((List<OrderedTicketResponse>) httpServletRequest.getSession().getAttribute("ordered-tickets-session"))
                .orElseGet(ArrayList::new);
    }

    public OrderedTicketResponse findById(final String orderedTicketId) {
        return this.orderedTicketRepository.findById(Long.parseLong(orderedTicketId == null ? "0" : orderedTicketId))
                .stream()
                .peek(orderedTicket -> log.info(ORDERED_TICKET_FOUND_BY_ID.getLogMessage(), orderedTicket))
                .map(this.orderedTicketMapper::toResponse)
                .findFirst()
                .orElseGet(OrderedTicketResponse::new);
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public OrderedTicketResponse orderTicketByIdentity(final OrderedTicketRequest orderedTicketRequest) {
        OrderedTicket orderedTicket = this.orderedTicketMapper.toEntity(orderedTicketRequest);
        this.flightSeatService.reserveFlightSeat(orderedTicket);
        return this.orderedTicketMapper.toResponse(this.orderedTicketRepository.save(orderedTicket));
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public OrderedTicketResponse orderTicketBySession(final OrderedTicketRequest orderedTicketRequest, final HttpServletRequest httpServletRequest) {
        List<OrderedTicketResponse> orderedTicketResponses = this.findAllInSession(httpServletRequest);
        OrderedTicket orderedTicket = this.orderedTicketMapper.toEntity(orderedTicketRequest);
        this.flightSeatService.reserveFlightSeat(orderedTicket);
        OrderedTicketResponse orderedTicketResponse = this.orderedTicketMapper.toResponse(
                this.orderedTicketRepository.save(orderedTicket)
        );
        orderedTicketResponses.add(orderedTicketResponse);
        httpServletRequest.getSession().setAttribute("ordered-tickets-session", orderedTicketResponses);
        return orderedTicketResponse;
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String cancelOrderedTicket(final String orderedTicketId) {
        return this.orderedTicketRepository.findById(Long.parseLong(orderedTicketId == null ? "0" : orderedTicketId))
                .map(
                        orderedTicket -> {
                            this.flightSeatService.cancelReserveFlightSeat(orderedTicket);
                            this.orderedTicketRepository.deleteById(orderedTicket.getId());
                            return ORDERED_TICKET_CANCELLED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new OrderedTicketNotFoundException(ORDERED_TICKET_NOT_FOUND.getMessage())
                );
    }
}
