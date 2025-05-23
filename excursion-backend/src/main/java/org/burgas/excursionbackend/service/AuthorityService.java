package org.burgas.excursionbackend.service;

import org.burgas.excursionbackend.dto.AuthorityRequest;
import org.burgas.excursionbackend.dto.AuthorityResponse;
import org.burgas.excursionbackend.exception.AuthorityNotCreatedException;
import org.burgas.excursionbackend.exception.AuthorityNotFoundException;
import org.burgas.excursionbackend.mapper.AuthorityMapper;
import org.burgas.excursionbackend.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.AuthorityLogs.*;
import static org.burgas.excursionbackend.message.AuthorityMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AuthorityService {

    private static final Logger log = LoggerFactory.getLogger(AuthorityService.class);
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    public List<AuthorityResponse> findAll() {
        return this.authorityRepository.findAll()
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_ALL.getLogMessage(), authority))
                .map(this.authorityMapper::toResponse)
                .peek(authorityResponse -> log.info(TRANSFORM_TO_AUTHORITY_RESPONSE.getLogMessage(), authorityResponse))
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AuthorityResponse>> findAllAsync() {
        return supplyAsync(this.authorityRepository::findAll)
                .thenApplyAsync(
                        authorities -> authorities
                                .stream()
                                .peek(authority -> log.info(AUTHORITY_FOUND_ASYNC.getLogMessage(), authority))
                                .map(this.authorityMapper::toResponse)
                                .peek(authorityResponse -> log.info(TRANSFORM_TO_AUTHORITY_RESPONSE_ASYNC.getLogMessage(), authorityResponse))
                                .collect(Collectors.toList())
                );
    }

    public AuthorityResponse findById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId == null ? "0" : authorityId))
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_BY_ID.getLogMessage(), authority))
                .findFirst()
                .map(this.authorityMapper::toResponse)
                .orElseGet(AuthorityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<AuthorityResponse> findByIdAsync(final String authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(Long.valueOf(authorityId == null ? "0" : authorityId)))
                .thenApplyAsync(authority -> authority.map(this.authorityMapper::toResponse))
                .thenApplyAsync(authorityResponse -> authorityResponse.orElseGet(AuthorityResponse::new));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AuthorityResponse createOrUpdate(final AuthorityRequest authorityRequest) {
        return ofNullable(this.authorityMapper.toEntity(authorityRequest))
                .map(this.authorityRepository::save)
                .map(this.authorityMapper::toResponse)
                .orElseThrow(
                        () -> new AuthorityNotCreatedException(AUTHORITY_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AuthorityResponse> createOrUpdateAsync(final AuthorityRequest authorityRequest) {
        return supplyAsync(() -> this.authorityMapper.toEntity(authorityRequest))
                .thenApplyAsync(this.authorityRepository::save)
                .thenApplyAsync(this.authorityMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId == null ? "0" : authorityId))
                .map(
                        authority -> {
                            this.authorityRepository.deleteById(authority.getId());
                            log.info(AUTHORITY_DELETED_BY_ID.getLogMessage(), authority);
                            return AUTHORITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(Long.valueOf(authorityId == null ? "0" : authorityId)))
                .thenApplyAsync(
                        authority -> authority
                                .map(
                                        checkAuthority -> {
                                            this.authorityRepository.deleteById(checkAuthority.getId());
                                            log.info(AUTHORITY_DELETED_BY_ID_ASYNC.getLogMessage(), authority);
                                            return AUTHORITY_DELETED.getMessage();
                                        }
                                )
                                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()))
                );
    }
}
