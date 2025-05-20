package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.AuthorityRequest;
import org.burgas.flightbackend.dto.AuthorityResponse;
import org.burgas.flightbackend.exception.AuthorityNotCreatedException;
import org.burgas.flightbackend.exception.AuthorityNotFoundException;
import org.burgas.flightbackend.mapper.AuthorityMapper;
import org.burgas.flightbackend.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.flightbackend.log.AuthorityLogs.*;
import static org.burgas.flightbackend.message.AuthorityMessages.*;
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
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AuthorityResponse>> findAllAsync() {
        return supplyAsync(this.authorityRepository::findAll)
                .thenApplyAsync(
                        authorities -> authorities.stream()
                                .peek(authority -> log.info(AUTHORITY_FOUND_ALL_ASYNC.getLogMessage(), authority))
                                .map(this.authorityMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public AuthorityResponse findById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId == null ? "0" : authorityId))
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_BY_ID.getLogMessage(), authority))
                .map(this.authorityMapper::toResponse)
                .findFirst()
                .orElseGet(AuthorityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<AuthorityResponse> findByIdAsync(final String authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(Long.parseLong(authorityId == null ? "0" : authorityId)))
                .thenApplyAsync(
                        authority -> authority.stream()
                                .peek(foundAuthority -> log.info(AUTHORITY_FOUND_BY_ID.getLogMessage(), foundAuthority))
                                .map(this.authorityMapper::toResponse)
                                .findFirst()
                                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND_ASYNC.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(final AuthorityRequest authorityRequest) {
        return of(this.authorityMapper.toEntity(authorityRequest))
                .map(this.authorityRepository::save)
                .map(this.authorityMapper::toResponse)
                .map(AuthorityResponse::getId)
                .orElseThrow(() -> new AuthorityNotCreatedException(AUTHORITY_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Long> createOrUpdateAsync(final AuthorityRequest authorityRequest) {
        return supplyAsync(() -> this.authorityMapper.toEntity(authorityRequest))
                .thenApplyAsync(this.authorityRepository::save)
                .thenApplyAsync(this.authorityMapper::toResponse)
                .thenApplyAsync(AuthorityResponse::getId);
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
                            log.info(AUTHORITY_DELETED_BY_ID.getLogMessage());
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
        return supplyAsync(() -> this.authorityRepository.findById(Long.parseLong(authorityId == null ? "0" : authorityId)))
                .thenApplyAsync(
                        authority -> authority.stream()
                                .peek(foundAuthority -> log.info(AUTHORITY_FOUND_BEFORE_DELETE.getLogMessage(), foundAuthority))
                                .map(
                                        foundAuthority -> {
                                            this.authorityRepository.deleteById(foundAuthority.getId());
                                            return AUTHORITY_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage())
                                )
                );
    }
}
