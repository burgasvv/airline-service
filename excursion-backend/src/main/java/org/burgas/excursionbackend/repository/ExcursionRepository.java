package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Excursion;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {

    List<Excursion> findExcursionsByGuideId(Long guideId);

    List<Excursion> findExcursionsByPassed(Boolean passed);

    Optional<Excursion> findExcursionByIdAndPassed(Long id, Boolean passed);

    @Query(
            nativeQuery = true,
            value = """
                    select e.* from excursion e join excursion_identity ei on e.id = ei.excursion_id
                                        where ei.identity_id = :identityId
                    """
    )
    List<Excursion> findExcursionsByIdentityId(Long identityId);

    @Override
    @NotNull
    Page<Excursion> findAll(@NotNull Pageable pageable);
}
