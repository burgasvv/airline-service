package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends R2dbcRepository<Address, Long> {
}
