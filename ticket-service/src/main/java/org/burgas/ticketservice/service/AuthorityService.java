package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.dto.AuthorityResponse;
import org.burgas.ticketservice.exception.AuthorityNotFoundException;
import org.burgas.ticketservice.mapper.AuthorityMapper;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static org.burgas.ticketservice.message.AuthorityMessage.AUTHORITY_DELETED;
import static org.burgas.ticketservice.message.AuthorityMessage.AUTHORITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;
import static reactor.core.publisher.SignalType.ON_COMPLETE;
import static reactor.core.publisher.SignalType.ON_NEXT;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AuthorityService {

    public static final String FIND_AUTHORITY = "Find authority";
    public static final String COMPLETE_RESPONSE = "Complete authority response";
    public static final String AUTHORITY_SAVED = "Authority saved";

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    public Flux<AuthorityResponse> findAll() {
        return this.authorityRepository
                .findAll()
                .log(FIND_AUTHORITY, INFO, ON_NEXT)
                .flatMap(
                        authority -> this.authorityMapper.toAuthorityResponse(
                                Mono.fromCallable(() -> authority)
                        )
                )
                .log(COMPLETE_RESPONSE, FINE, ON_COMPLETE);
    }

    public Mono<AuthorityResponse> findById(final String authorityId) {
        return this.authorityRepository
                .findById(Long.valueOf(authorityId))
                .log(FIND_AUTHORITY, INFO, ON_NEXT)
                .flatMap(
                        authority -> this.authorityMapper.toAuthorityResponse(
                                Mono.fromCallable(() -> authority)
                        )
                )
                .log(COMPLETE_RESPONSE, FINE, ON_COMPLETE);
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<Long> createOrUpdate(final Mono<AuthorityRequest> authorityRequestMono) {
        return authorityRequestMono
                .flatMap(
                        authorityRequest ->
                                this.authorityMapper.toAuthority(Mono.fromCallable(() -> authorityRequest))
                                        .flatMap(authorityRepository::save)
                                        .log(AUTHORITY_SAVED, INFO, ON_NEXT)
                                        .flatMap(
                                                authority -> this.authorityMapper.toAuthorityResponse(
                                                        Mono.fromCallable(() -> authority)
                                                )
                                        )
                                        .log(COMPLETE_RESPONSE)
                                        .flatMap(authorityResponse -> Mono.fromCallable(authorityResponse::getId))
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteById(final String authorityId) {
        return this.authorityRepository
                .findById(Long.valueOf(authorityId))
                .flatMap(
                        authority -> this.authorityRepository
                                .deleteById(requireNonNull(authority.getId()))
                                .thenReturn(AUTHORITY_DELETED.getMessage())
                )
                .switchIfEmpty(
                        Mono.error(new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()))
                );
    }
}
