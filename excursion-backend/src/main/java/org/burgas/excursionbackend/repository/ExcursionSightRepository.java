package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.ExcursionSight;
import org.burgas.excursionbackend.entity.ExcursionSightPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionSightRepository extends JpaRepository<ExcursionSight, ExcursionSightPK> {

    void deleteExcursionSightsByExcursionId(Long excursionId);
}
