package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.dto.AuthorityResponse;
import org.burgas.ticketservice.entity.Authority;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class AuthorityMapper implements MapperDataHandler {

    private final AuthorityRepository authorityRepository;

    public AuthorityMapper(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Mono<Authority> toAuthority(final Mono<AuthorityRequest> authorityRequestMono) {
        return authorityRequestMono
                .flatMap(
                        authorityRequest -> {
                            Long authorityId = this.getData(authorityRequest.getId(), 0L);
                            return this.authorityRepository
                                    .findById(authorityId)
                                    .flatMap(
                                            authority -> Mono.fromCallable(
                                                    () -> Authority.builder()
                                                            .id(authority.getId())
                                                            .name(this.getData(authorityRequest.getName(), authority.getName()))
                                                            .isNew(false)
                                                            .build()
                                            )
                                    )
                                    .switchIfEmpty(
                                            Mono.fromCallable(
                                                    () -> Authority.builder()
                                                            .name(authorityRequest.getName())
                                                            .isNew(true)
                                                            .build()
                                            )
                                    );
                        }
                );
    }

    public Mono<AuthorityResponse> toAuthorityResponse(final Mono<Authority> authorityMono) {
        return authorityMono
                .flatMap(
                        authority -> Mono.fromCallable(
                                () -> AuthorityResponse.builder()
                                        .id(authority.getId())
                                        .name(authority.getName())
                                        .build()
                        )
                );
    }
}
