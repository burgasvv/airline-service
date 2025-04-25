package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FlightRequest;
import org.burgas.ticketservice.dto.FlightResponse;
import org.burgas.ticketservice.entity.Employee;
import org.burgas.ticketservice.entity.Flight;
import org.burgas.ticketservice.mapper.FlightMapper;
import org.burgas.ticketservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.burgas.ticketservice.message.FlightMessage.EMPLOYEE_ADDED_TO_FLIGHT;
import static org.burgas.ticketservice.message.FlightMessage.EMPLOYEE_REMOVED_FROM_FLIGHT;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final EmployeeRepository employeeRepository;
    private final PlaneRepository planeRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final TicketRepository ticketRepository;

    public FlightService(
            FlightRepository flightRepository, FlightMapper flightMapper, EmployeeRepository employeeRepository,
            PlaneRepository planeRepository, FlightSeatRepository flightSeatRepository, TicketRepository ticketRepository
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.employeeRepository = employeeRepository;
        this.planeRepository = planeRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<FlightResponse> findAll() {
        return this.flightRepository.findAll()
                .stream()
                .map(this.flightMapper::toFlightResponse)
                .toList();
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCityAndDepartureDate(final String departureCityId, final String arrivalCityId, final String departureDate) {
        return this.flightRepository.findFlightsByDepartureCityAndArrivalCityAndDepartureDate(
                        Long.parseLong(departureCityId), Long.parseLong(arrivalCityId),
                        departureDate != null && !departureDate.isBlank() ? LocalDate.parse(departureDate) : null)
                .stream()
                .map(this.flightMapper::toFlightResponse)
                .toList();
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCity(final String departureCityId, final String arrivalCityId) {
        return this.flightRepository.findFlightsByDepartureCityIdAndArrivalCityId(Long.parseLong(departureCityId), Long.parseLong(arrivalCityId))
                .stream()
                .map(this.flightMapper::toFlightResponse)
                .toList();
    }

    public List<FlightResponse> findAllByDepartureCityAndArrivalCityBack(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .map(
                        flight -> Optional.of(this.flightMapper.toFlightResponse(flight))
                                .map(
                                        flightResponse -> this.flightRepository.findFlightsByDepartureCityIdAndArrivalCityIdBack(
                                                flightResponse.getArrival().getAddress().getCity().getId(),
                                                flightResponse.getDeparture().getAddress().getCity().getId(),
                                                flight.getArrivalAt()
                                        )
                                                .stream()
                                                .map(this.flightMapper::toFlightResponse)
                                                .toList()
                                )
                                .orElseThrow()
                )
                .orElseThrow();
    }

    public FlightResponse findById(final String flightId) {
        return this.flightRepository.findById(Long.parseLong(flightId))
                .map(this.flightMapper::toFlightResponse)
                .orElseGet(FlightResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FlightResponse createOrUpdate(final FlightRequest flightRequest) {
        return Optional.of(this.flightMapper.toFlight(flightRequest))
                .map(this.flightRepository::save)
                .map(this.flightMapper::toFlightResponse)
                .orElseGet(FlightResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String addEmployeeToFlight(final String flightId, final String employeeId) {
        Flight flight = this.flightRepository.findById(Long.parseLong(flightId)).orElse(null);
        Employee employee = this.employeeRepository.findById(Long.parseLong(employeeId)).orElse(null);
        this.flightRepository.insertIntoFlightEmployee(requireNonNull(flight).getId(), requireNonNull(employee).getId());
        return EMPLOYEE_ADDED_TO_FLIGHT.getMessage();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String removeEmployeeFromFlight(final String flightId, final String employeeId) {
        this.flightRepository.deleteFromFlightEmployee(Long.parseLong(flightId), Long.parseLong(employeeId));
        return EMPLOYEE_REMOVED_FROM_FLIGHT.getMessage();
    }
}
