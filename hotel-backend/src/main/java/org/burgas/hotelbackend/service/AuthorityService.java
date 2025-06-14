package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.AuthorityRequest;
import org.burgas.hotelbackend.dto.AuthorityResponse;
import org.burgas.hotelbackend.exception.AuthorityNotCreatedException;
import org.burgas.hotelbackend.exception.AuthorityNotFoundException;
import org.burgas.hotelbackend.mapper.AuthorityMapper;
import org.burgas.hotelbackend.repository.AuthorityRepository;
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
import static org.burgas.hotelbackend.log.AuthorityLogs.*;
import static org.burgas.hotelbackend.message.AuthorityMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
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
                                .peek(foundAuthority -> log.info(AUTHORITY_FOUND_ALL_ASYNC.getLogMessage(), foundAuthority))
                                .map(this.authorityMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public AuthorityResponse findById(final Long authorityId) {
        return this.authorityRepository.findById(authorityId == null ? 0L : authorityId)
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_BY_ID_ASYNC.getLogMessage(), authority))
                .map(this.authorityMapper::toResponse)
                .findFirst()
                .orElseGet(AuthorityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<AuthorityResponse> findByIdAsync(final Long authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(authorityId == null ? 0L : authorityId))
                .thenApplyAsync(
                        authority -> authority.stream()
                                .peek(foundAuthority -> log.info(AUTHORITY_FOUND_BY_ID_ASYNC.getLogMessage(), foundAuthority))
                                .map(this.authorityMapper::toResponse)
                                .findFirst()
                                .orElseGet(AuthorityResponse::new)
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AuthorityResponse createOrUpdate(final AuthorityRequest authorityRequest) {
        return of(this.authorityMapper.toEntity(authorityRequest))
                .map(this.authorityRepository::save)
                .map(this.authorityMapper::toResponse)
                .orElseThrow(() -> new AuthorityNotCreatedException(AUTHORITY_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AuthorityResponse> createOrUpdateAsync(final AuthorityRequest authorityRequest) {
        return supplyAsync(() -> this.authorityMapper.toEntity(authorityRequest))
                .thenApplyAsync(this.authorityRepository::save)
                .thenApplyAsync(this.authorityMapper::toResponse);
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long authorityId) {
        return this.authorityRepository.findById(authorityId == null ? 0L : authorityId)
                .map(
                        authority -> {
                            this.authorityRepository.deleteById(authority.getId());
                            return AUTHORITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(authorityId == null ? 0L : authorityId))
                .thenApplyAsync(
                        authority -> authority.stream()
                                .peek(foundAuthority -> log.info(AUTHORITY_FOUND_BEFORE_DELETE.getLogMessage(), foundAuthority))
                                .map(
                                        foundAuthority -> {
                                            this.authorityRepository.deleteById(foundAuthority.getId());
                                            return AUTHORITY_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()))
                );
    }
}
