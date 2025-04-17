package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.Sight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SightRepository extends JpaRepository<Sight, Long> {
}
