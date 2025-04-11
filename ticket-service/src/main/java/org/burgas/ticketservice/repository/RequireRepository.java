package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Require;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RequireRepository extends R2dbcRepository<Require, Long> {

    Flux<Require> findRequiresByClosed(Boolean closed);
}
