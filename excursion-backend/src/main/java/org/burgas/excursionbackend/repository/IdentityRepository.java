package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Identity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    @Query(
            nativeQuery = true,
            value = """
                    select i.* from excursion_backend.public.identity i join public.excursion_identity ei on i.id = ei.identity_id
                                        where ei.excursion_id = :excursionId
                    """
    )
    List<Identity> findIdentitiesByExcursionId(Long excursionId);

    @Override
    @NotNull
    Page<Identity> findAll(@NotNull Pageable pageable);
}
