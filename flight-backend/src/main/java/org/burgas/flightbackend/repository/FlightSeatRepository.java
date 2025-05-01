package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

    List<FlightSeat> findFlightSeatsByFlightId(Long flightId);
}
