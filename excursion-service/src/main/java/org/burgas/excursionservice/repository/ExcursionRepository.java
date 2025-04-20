package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {

    List<Excursion> findExcursionsByGuideId(Long guideId);

    List<Excursion> findExcursionsByPassed(Boolean passed);

    @Query(
            nativeQuery = true,
            value = """
                    select e.* from excursion e join excursion_identity ei on e.id = ei.excursion_id
                                        where ei.identity_id = :identityId
                    """
    )
    List<Excursion> findExcursionsByIdentityId(Long identityId);
}
