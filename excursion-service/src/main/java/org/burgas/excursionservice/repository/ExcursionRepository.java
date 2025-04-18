package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {

    List<Excursion> findExcursionsByGuideId(Long guideId);
}
