package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RestoreToken;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RestoreTokenRepository extends R2dbcRepository<RestoreToken, Long> {

    Mono<RestoreToken> findRestoreTokenByIdentityId(Long identityId);

    Mono<Boolean> existsRestoreTokenByValueAndIdentityId(UUID value, Long identityId);

    Mono<Void> deleteRestoreTokenByValueAndIdentityId(UUID value, Long identityId);
}
