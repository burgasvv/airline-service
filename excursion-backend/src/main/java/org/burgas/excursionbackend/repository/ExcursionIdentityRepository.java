package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.ExcursionIdentity;
import org.burgas.excursionbackend.entity.ExcursionIdentityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionIdentityRepository extends JpaRepository<ExcursionIdentity, ExcursionIdentityPK> {

    Boolean existsExcursionIdentityByExcursionIdAndIdentityId(Long excursionId, Long identityId);
}
