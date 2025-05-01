package org.burgas.flightbackend.service;

import org.burgas.flightbackend.entity.RestoreToken;
import org.burgas.flightbackend.repository.RestoreTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RestoreToken createOrUpdateByIdentityId(final Long identityId) {
        return this.restoreTokenRepository
                .findRestoreTokenByIdentityId(identityId)
                .map(
                        restoreToken -> this.restoreTokenRepository.save(
                                RestoreToken.builder()
                                        .id(restoreToken.getId())
                                        .value(UUID.randomUUID())
                                        .identityId(restoreToken.getIdentityId())
                                        .build()
                        )
                )
                .orElseGet(
                        () -> this.restoreTokenRepository.save(
                                RestoreToken.builder()
                                        .identityId(identityId)
                                        .value(UUID.randomUUID())
                                        .build()
                        )
                );
    }
}
