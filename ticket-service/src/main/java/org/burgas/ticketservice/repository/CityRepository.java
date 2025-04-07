package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.City;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends R2dbcRepository<City, Long> {
}
