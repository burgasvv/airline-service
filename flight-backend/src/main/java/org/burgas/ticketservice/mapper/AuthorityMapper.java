package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.dto.AuthorityResponse;
import org.burgas.ticketservice.entity.Authority;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AuthorityMapper implements MapperDataHandler {

    private final AuthorityRepository authorityRepository;

    public AuthorityMapper(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority toAuthority(final AuthorityRequest authorityRequest) {
        Long authorityId = this.getData(authorityRequest.getId(), 0L);
        return this.authorityRepository
                .findById(authorityId)
                .map(
                        authority -> Authority.builder()
                                .id(authority.getId())
                                .name(this.getData(authorityRequest.getName(), authority.getName()))
                                .build()
                )
                .orElseGet(
                        () -> Authority.builder()
                                .name(authorityRequest.getName())
                                .build()
                );
    }

    public AuthorityResponse toAuthorityResponse(final Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .name(authority.getName())
                .build();
    }
}
