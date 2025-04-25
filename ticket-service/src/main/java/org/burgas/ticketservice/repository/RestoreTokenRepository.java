package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RestoreToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
@Repository
public interface RestoreTokenRepository extends JpaRepository<RestoreToken, Long> {

    Optional<RestoreToken> findRestoreTokenByIdentityId(Long identityId);

    Optional<Boolean> existsRestoreTokenByValueAndIdentityId(UUID value, Long identityId);

    void deleteRestoreTokenByValueAndIdentityId(UUID value, Long identityId);
}
