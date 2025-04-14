package org.burgas.excursionservice.service;

import org.burgas.excursionservice.dto.IdentityRequest;
import org.burgas.excursionservice.dto.IdentityResponse;
import org.burgas.excursionservice.entity.Identity;
import org.burgas.excursionservice.exception.IdentityNotFoundException;
import org.burgas.excursionservice.mapper.IdentityMapper;
import org.burgas.excursionservice.repository.IdentityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.message.IdentityMessage.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public IdentityService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .stream()
                .peek(identity -> log.info("Identity was found: {}", identity))
                .map(this.identityMapper::toIdentityResponse)
                .toList();
    }

    @Async
    public CompletableFuture<List<IdentityResponse>> findAllAsync() {
        return supplyAsync(this.identityRepository::findAll)
                .thenApplyAsync(
                        identities -> identities.stream()
                                .peek(identity -> log.info("Identity was found async: {}", identity))
                                .map(this.identityMapper::toIdentityResponse)
                                .toList()
                );
    }

    public IdentityResponse findById(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .stream()
                .peek(identity -> log.info("Identity was found by id: {}", identity))
                .map(this.identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    @Async
    public CompletableFuture<IdentityResponse> findByIdAsync(final String identityId) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info("Identity was found by id async: {}", foundIdentity))
                                .map(this.identityMapper::toIdentityResponse)
                                .findFirst()
                )
                .thenApplyAsync(identityResponse -> identityResponse.orElseGet(IdentityResponse::new));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(final IdentityRequest identityRequest) {
        return ofNullable(this.identityMapper.toIdentity(identityRequest))
                .map(this.identityRepository::save)
                .stream()
                .peek(identity -> log.info("Identity was saved: {}", identity))
                .map(this.identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<IdentityResponse> createOrUpdateAsync(final IdentityRequest identityRequest) {
        return supplyAsync(() -> this.identityMapper.toIdentity(identityRequest))
                .thenApplyAsync(this.identityRepository::save)
                .thenApplyAsync(this.identityMapper::toIdentityResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            log.info("Identity was found before delete: {}", identity);
                            this.identityRepository.deleteById(identity.getId());
                            log.info("Identity was deleted");
                            return IDENTITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String identityId) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity
                                .map(
                                        foundIdentity -> {
                                            log.info("Identity was found before delete async: {}", foundIdentity);
                                            this.identityRepository.deleteById(foundIdentity.getId());
                                            log.info("Identity was deleted async");
                                            return IDENTITY_DELETED.getMessage();
                                        }
                                )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String accountControl(final String identityId, final String enable) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            log.info("Identity was found for control: {}", identity);
                            identity.setEnabled(Boolean.valueOf(enable));
                            Identity saved = this.identityRepository.save(identity);
                            return saved.getEnabled() ? IDENTITY_ENABLED.getMessage() : IDENTITY_NOT_ENABLED.getMessage();
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> accountControlAsync(final String identityId, final String enable) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.map(
                                        foundIdentity -> {
                                            log.info("Identity was found for control async: {}", foundIdentity);
                                            foundIdentity.setEnabled(Boolean.valueOf(enable));
                                            return this.identityRepository.save(foundIdentity);
                                        }
                                )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                )
                .thenApplyAsync(identity -> identity.getEnabled() ? IDENTITY_ENABLED.getMessage() : IDENTITY_NOT_ENABLED.getMessage());
    }
}
