package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Position;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends R2dbcRepository<Position, Long> {
}
