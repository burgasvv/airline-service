package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Image;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends R2dbcRepository<Image, Long> {
}
