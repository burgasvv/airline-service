package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

    List<FlightSeat> findFlightSeatsByFlightId(Long flightId);
}
