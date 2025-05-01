package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.FlightRequest;
import org.burgas.flightbackend.dto.FlightResponse;
import org.burgas.flightbackend.entity.*;
import org.burgas.flightbackend.exception.*;
import org.burgas.flightbackend.log.FlightLogs;
import org.burgas.flightbackend.mapper.FlightMapper;
import org.burgas.flightbackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.burgas.flightbackend.log.FlightLogs.FLIGHT_FOUND_ALL;
import static org.burgas.flightbackend.log.FlightLogs.FLIGHT_FOUND_BY_DEPARTURE_AND_ARRIVAL;
import static org.burgas.flightbackend.message.EmployeeMessages.EMPLOYEE_NOT_FOUND;
import static org.burgas.flightbackend.message.FlightMessages.*;
import static org.burgas.flightbackend.message.PlaneMessages.PLANE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FlightService {

    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final EmployeeRepository employeeRepository;
    private final PlaneRepository planeRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final TicketRepository ticketRepository;
    private final FlightEmployeeRepository flightEmployeeRepository;

    public FlightService(
            FlightRepository flightRepository, FlightMapper flightMapper, EmployeeRepository employeeRepository,
            PlaneRepository planeRepository, FlightSeatRepository flightSeatRepository,
            TicketRepository ticketRepository, FlightEmployeeRepository flightEmployeeRepository
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.employeeRepository = employeeRepository;
        this.planeRepository = planeRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.ticketRepository = ticketRepository;
        this.flightEmployeeRepository = flightEmployeeRepository;
    }

    public List<FlightResponse> findAll() {
        return this.flightRepository.findAll()
                .stream()
                .peek(flight -> log.info(FLIGHT_FOUND_ALL.getLogMessage(), flight))
                .map(this.flightMapper::toFlightResponse)
                .collect(Collectors.toList());
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCityAndDepartureDate(
            final String departureCityId, final String arrivalCityId, final String departureDate
    ) {
        return this.flightRepository.findFlightsByDepartureCityAndArrivalCityAndDepartureDate(
                Long.parseLong(departureCityId), Long.parseLong(arrivalCityId),
                        departureDate != null && !departureDate.isBlank() ? LocalDate.parse(departureDate) : null)
                .stream()
                .peek(flight -> log.info(FLIGHT_FOUND_BY_DEPARTURE_AND_ARRIVAL.getLogMessage(), flight))
                .map(this.flightMapper::toFlightResponse)
                .collect(Collectors.toList());
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCity(final String departureCityId, final String arrivalCityId) {
        return this.flightRepository.findFlightsByDepartureCityIdAndArrivalCityId(Long.parseLong(departureCityId), Long.parseLong(arrivalCityId))
                .stream()
                .peek(flight -> log.info(FLIGHT_FOUND_BY_DEPARTURE_AND_ARRIVAL.getLogMessage(),flight))
                .map(this.flightMapper::toFlightResponse)
                .collect(Collectors.toList());
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCityBack(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .map(
                        flight -> of(this.flightMapper.toFlightResponse(flight))
                                .map(
                                        flightResponse -> this.flightRepository.findFlightsByDepartureCityIdAndArrivalCityIdBack(
                                                flightResponse.getArrival().getAddress().getCity().getId(),
                                                flightResponse.getDeparture().getAddress().getCity().getId(),
                                                flight.getArrivalAt()
                                        )
                                                .stream()
                                                .map(this.flightMapper::toFlightResponse)
                                                .collect(Collectors.toList())
                                )
                                .orElseThrow(
                                        () -> new FlightNotTransformedException(FLIGHT_NOT_TRANSFORMED.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new FlightNotFoundException(FLIGHT_NOT_FOUND.getMessage())
                );
    }

    public FlightResponse findById(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .stream()
                .peek(flight -> log.info(FlightLogs.FLIGHT_FOUND_BY_ID.getLogMessage(), flight))
                .map(this.flightMapper::toFlightResponse)
                .findFirst()
                .orElseGet(FlightResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FlightResponse createOrUpdate(final FlightRequest flightRequest) {
        FlightResponse flightResponse = of(this.flightMapper.toFlight(flightRequest))
                .map(this.flightRepository::save)
                .map(this.flightMapper::toFlightResponse)
                .orElseThrow(
                        () -> new FlightNotCreatedException(FLIGHT_NOT_CREATED.getMessage())
                );

        return this.planeRepository.findById(flightResponse.getPlane().getId())
                .map(
                        plane -> {
                            IntStream.rangeClosed(1, Math.toIntExact(plane.getBusinessClass()))
                                    .forEach(
                                            value -> this.flightSeatRepository.save(
                                                    FlightSeat.builder()
                                                            .number((long) value)
                                                            .flightId(flightResponse.getId())
                                                            .cabinTypeId(1L)
                                                            .purchased(false)
                                                            .closed(false)
                                                            .build()
                                            )
                                    );

                            IntStream.rangeClosed(
                                            Math.toIntExact(plane.getBusinessClass() + 1),
                                            Math.toIntExact(plane.getBusinessClass() + plane.getEconomyClass())
                                    )
                                    .forEach(
                                            value -> this.flightSeatRepository.save(
                                                    FlightSeat.builder()
                                                            .number((long) value)
                                                            .flightId(flightResponse.getId())
                                                            .cabinTypeId(2L)
                                                            .purchased(false)
                                                            .closed(false)
                                                            .build()
                                            )
                                    );

                            this.ticketRepository.save(
                                    Ticket.builder()
                                            .flightId(flightResponse.getId())
                                            .amount(plane.getBusinessClass())
                                            .price(flightRequest.getBusinessPrice())
                                            .cabinTypeId(1L)
                                            .closed(false)
                                            .build()
                            );
                            this.ticketRepository.save(
                                    Ticket.builder()
                                            .flightId(flightResponse.getId())
                                            .amount(plane.getEconomyClass())
                                            .price(flightRequest.getEconomyPrice())
                                            .cabinTypeId(2L)
                                            .closed(false)
                                            .build()
                            );

                            return flightResponse;
                        }
                )
                .orElseThrow(
                        () -> new PlaneNotFoundException(PLANE_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addEmployeeToFlight(final String flightId, final String employeeId) {
        Flight flight = this.flightRepository.findById(Long.parseLong(flightId))
                .orElseThrow(
                        () -> new FlightNotFoundException(FLIGHT_NOT_FOUND.getMessage())
                );
        Employee employee = this.employeeRepository.findById(Long.parseLong(employeeId))
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        this.flightEmployeeRepository.save(
                FlightEmployee.builder().flightId(flight.getId()).employeeId(employee.getId()).build()
        );
        return EMPLOYEE_ADDED_TO_FLIGHT.getMessage();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeEmployeeFromFlight(final String flightId, final String employeeId) {
        Flight flight = this.flightRepository.findById(Long.parseLong(flightId))
                .orElseThrow(
                        () -> new FlightNotFoundException(FLIGHT_NOT_FOUND.getMessage())
                );
        Employee employee = this.employeeRepository.findById(Long.parseLong(employeeId))
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
        this.flightEmployeeRepository.deleteById(
                FlightEmployeePK.builder().flightId(flight.getId()).employeeId(employee.getId()).build()
        );
        return EMPLOYEE_REMOVED_FROM_FLIGHT.getMessage();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String startFlight(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .map(
                        flight -> {
                            flight.setInProgress(true);
                            Flight saved = this.flightRepository.save(flight);
                            this.ticketRepository.findTicketsByFlightId(saved.getId())
                                    .forEach(
                                            ticket -> {
                                                ticket.setClosed(true);
                                                this.ticketRepository.save(ticket);
                                            }
                                    );
                            this.flightSeatRepository.findFlightSeatsByFlightId(saved.getId())
                                    .forEach(
                                            flightSeat -> {
                                                flightSeat.setClosed(true);
                                                this.flightSeatRepository.save(flightSeat);
                                            }
                                    );
                            return format(FLIGHT_STARTS.getMessage(), saved.getId());
                        }
                )
                .orElseThrow(
                        () -> new FlightNotFoundException(FLIGHT_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String completeFlight(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .map(
                        flight -> {
                            flight.setInProgress(false);
                            flight.setCompleted(true);
                            Flight saved = this.flightRepository.save(flight);
                            return format(FLIGHT_COMPLETE.getMessage(), saved.getId());
                        }
                )
                .orElseThrow(
                        () -> new FlightNotFoundException(FLIGHT_NOT_FOUND.getMessage())
                );
    }
}
