package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.dto.FlightRequest;
import org.burgas.ticketservice.dto.FlightResponse;
import org.burgas.ticketservice.dto.PlaneResponse;
import org.burgas.ticketservice.entity.Flight;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.AirportRepository;
import org.burgas.ticketservice.repository.FlightRepository;
import org.burgas.ticketservice.repository.PlaneRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class FlightMapper implements MapperDataHandler {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;

    public FlightMapper(
            FlightRepository flightRepository, AirportRepository airportRepository, AirportMapper airportMapper,
            PlaneRepository planeRepository, PlaneMapper planeMapper
    ) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.airportMapper = airportMapper;
        this.planeRepository = planeRepository;
        this.planeMapper = planeMapper;
    }

    public Flight toFlight(final FlightRequest flightRequest) {
        Long flightId = this.getData(flightRequest.getId(), 0L);
        return this.flightRepository.findById(flightId)
                .map(
                        flight -> Flight.builder()
                                .id(flight.getId())
                                .number(flight.getNumber())
                                .departureId(this.getData(flightRequest.getDepartureId(), flight.getDepartureId()))
                                .arrivalId(this.getData(flightRequest.getArrivalId(), flight.getArrivalId()))
                                .planeId(this.getData(flightRequest.getPlaneId(), flight.getPlaneId()))
                                .departureAt(this.getData(flightRequest.getDepartureAt(), flight.getDepartureAt()))
                                .arrivalAt(this.getData(flightRequest.getArrivalAt(), flight.getArrivalAt()))
                                .inProgress(this.getData(flightRequest.getInProgress(), flight.getInProgress()))
                                .completed(this.getData(flightRequest.getCompleted(), flight.getCompleted()))
                                .build()
                )
                .orElseGet(
                        () -> Flight.builder()
                                .number(
                                        UUID.randomUUID()
                                                .toString()
                                                .replaceAll("-", "")
                                                .substring(0, 10)
                                )
                                .departureId(flightRequest.getDepartureId())
                                .arrivalId(flightRequest.getArrivalId())
                                .planeId(flightRequest.getPlaneId())
                                .departureAt(flightRequest.getDepartureAt())
                                .arrivalAt(flightRequest.getArrivalAt())
                                .inProgress(flightRequest.getInProgress())
                                .completed(flightRequest.getCompleted())
                                .build()
                );
    }

    public FlightResponse toFlightResponse(final Flight flight) {
        return FlightResponse.builder()
                .id(flight.getId())
                .number(flight.getNumber())
                .departure(
                        this.airportRepository.findById(flight.getDepartureId())
                                .map(this.airportMapper::toAirportResponse)
                                .orElseGet(AirportResponse::new)
                )
                .arrival(
                        this.airportRepository.findById(flight.getArrivalId())
                                .map(this.airportMapper::toAirportResponse)
                                .orElseGet(AirportResponse::new)
                )
                .plane(
                        this.planeRepository.findById(flight.getPlaneId())
                                .map(this.planeMapper::toPlaneResponse)
                                .orElseGet(PlaneResponse::new)
                )
                .departureAt(flight.getDepartureAt().format(ofPattern("dd.MM.yyyy, hh:mm")))
                .arrivalAt(flight.getArrivalAt().format(ofPattern("dd.MM.yyyy, hh:ss")))
                .inProgress(flight.getInProgress())
                .completed(flight.getCompleted())
                .build();
    }
}
