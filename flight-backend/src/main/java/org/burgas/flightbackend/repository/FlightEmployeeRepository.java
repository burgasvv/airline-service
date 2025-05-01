package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.FlightEmployee;
import org.burgas.flightbackend.entity.FlightEmployeePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightEmployeeRepository extends JpaRepository<FlightEmployee, FlightEmployeePK> {
}
