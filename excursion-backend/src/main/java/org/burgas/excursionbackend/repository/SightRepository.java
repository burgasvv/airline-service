package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Sight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SightRepository extends JpaRepository<Sight, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select s.* from sight s join excursion_sight es on s.id = es.sight_id
                                        where es.excursion_id = :excursionId
                    """
    )
    List<Sight> findSightsByExcursionId(Long excursionId);
}
