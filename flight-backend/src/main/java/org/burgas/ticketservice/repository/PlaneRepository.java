package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {

    List<Plane> findPlanesByFree(Boolean free);
}
