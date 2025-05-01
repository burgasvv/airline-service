package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.Identity;
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
                    select i.* from excursion_service.public.identity i join public.excursion_identity ei on i.id = ei.identity_id
                                        where ei.excursion_id = :excursionId
                    """
    )
    List<Identity> findIdentitiesByExcursionId(Long excursionId);
}
