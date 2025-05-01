package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.ExcursionIdentity;
import org.burgas.excursionservice.entity.ExcursionIdentityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionIdentityRepository extends JpaRepository<ExcursionIdentity, ExcursionIdentityPK> {

    Boolean existsExcursionIdentityByExcursionIdAndIdentityId(Long excursionId, Long identityId);
}
