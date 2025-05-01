package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.CabinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinTypeRepository extends JpaRepository<CabinType, Long> {
}
