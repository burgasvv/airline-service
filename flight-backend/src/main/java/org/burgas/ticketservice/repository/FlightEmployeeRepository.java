package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.FlightEmployee;
import org.burgas.ticketservice.entity.FlightEmployeePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightEmployeeRepository extends JpaRepository<FlightEmployee, FlightEmployeePK> {
}
