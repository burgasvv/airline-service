package org.burgas.excursionservice.service;

import org.burgas.excursionservice.dto.AuthorityRequest;
import org.burgas.excursionservice.dto.AuthorityResponse;
import org.burgas.excursionservice.exception.AuthorityNotFoundException;
import org.burgas.excursionservice.mapper.AuthorityMapper;
import org.burgas.excursionservice.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.message.AuthorityMessages.AUTHORITY_DELETED;
import static org.burgas.excursionservice.message.AuthorityMessages.AUTHORITY_NOT_FOUND;
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
                .peek(authority -> log.info("Authority was found: {}", authority))
                .map(this.authorityMapper::toAuthorityResponse)
                .peek(authorityResponse -> log.info("Transform to authority response: {}", authorityResponse))
                .toList();
    }

    @Async
    public CompletableFuture<List<AuthorityResponse>> findAllAsync() {
        return supplyAsync(this.authorityRepository::findAll)
                .thenApplyAsync(
                        authorities -> authorities
                                .stream()
                                .peek(authority -> log.info("Find authority async: {}", authority))
                                .map(this.authorityMapper::toAuthorityResponse)
                                .peek(authorityResponse -> log.info("Transform to authority response async: {}", authorityResponse))
                                .toList()
                );
    }

    public AuthorityResponse findById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId))
                .stream()
                .peek(authority -> log.info("Authority was found by id: {}", authority))
                .findFirst()
                .map(this.authorityMapper::toAuthorityResponse)
                .orElseGet(AuthorityResponse::new);
    }

    @Async
    public CompletableFuture<AuthorityResponse> findByIdAsync(final String authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(Long.valueOf(authorityId)))
                .thenApplyAsync(authority -> authority.map(this.authorityMapper::toAuthorityResponse))
                .thenApplyAsync(authorityResponse -> authorityResponse.orElseGet(AuthorityResponse::new));
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AuthorityResponse createOrUpdate(final AuthorityRequest authorityRequest) {
        return ofNullable(this.authorityMapper.toAuthority(authorityRequest))
                .map(this.authorityRepository::save)
                .map(this.authorityMapper::toAuthorityResponse)
                .orElseGet(AuthorityResponse::new);
    }

    @Async
    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AuthorityResponse> createOrUpdateAsync(final AuthorityRequest authorityRequest) {
        return supplyAsync(() -> this.authorityMapper.toAuthority(authorityRequest))
                .thenApplyAsync(this.authorityRepository::save)
                .thenApplyAsync(this.authorityMapper::toAuthorityResponse);
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId))
                .map(
                        authority -> {
                            this.authorityRepository.deleteById(authority.getId());
                            log.info("Authority was deleted by id: {}", authority);
                            return AUTHORITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()));
    }

    @Async
    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String authorityId) {
        return supplyAsync(() -> this.authorityRepository.findById(Long.valueOf(authorityId)))
                .thenApplyAsync(
                        authority -> authority
                                .map(
                                        checkAuthority -> {
                                            this.authorityRepository.deleteById(checkAuthority.getId());
                                            log.info("Authority was deleted by id async: {}", authority);
                                            return AUTHORITY_DELETED.getMessage();
                                        }
                                )
                                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()))
                );
    }
}
