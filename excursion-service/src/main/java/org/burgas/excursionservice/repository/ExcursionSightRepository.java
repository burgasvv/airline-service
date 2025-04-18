package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.ExcursionSight;
import org.burgas.excursionservice.entity.ExcursionSightPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionSightRepository extends JpaRepository<ExcursionSight, ExcursionSightPK> {

    void deleteExcursionSightsByExcursionId(Long excursionId);
}
