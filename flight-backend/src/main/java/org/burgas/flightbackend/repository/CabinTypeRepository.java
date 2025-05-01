package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.CabinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinTypeRepository extends JpaRepository<CabinType, Long> {
}
