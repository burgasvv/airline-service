package org.burgas.ticketservice.service;

import org.burgas.ticketservice.entity.RestoreToken;
import org.burgas.ticketservice.repository.RestoreTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RestoreTokenService {

    private final RestoreTokenRepository restoreTokenRepository;

    public RestoreTokenService(RestoreTokenRepository restoreTokenRepository) {
        this.restoreTokenRepository = restoreTokenRepository;
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<RestoreToken> createOrUpdateByIdentityId(final Long identityId) {
        return this.restoreTokenRepository
                .findRestoreTokenByIdentityId(identityId)
                .flatMap(
                        restoreToken -> this.restoreTokenRepository.save(
                                RestoreToken.builder()
                                        .id(restoreToken.getId())
                                        .value(UUID.randomUUID())
                                        .identityId(restoreToken.getIdentityId())
                                        .isNew(false)
                                        .build()
                        )
                )
                .switchIfEmpty(
                        this.restoreTokenRepository.save(
                                RestoreToken.builder()
                                        .identityId(identityId)
                                        .value(UUID.randomUUID())
                                        .isNew(true)
                                        .build()
                        )
                );
    }
}
