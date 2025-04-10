package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Department;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends R2dbcRepository<Department, Long> {
}
