package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.AuthorityRequest;
import org.burgas.hotelbackend.dto.AuthorityResponse;
import org.burgas.hotelbackend.entity.Authority;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.AuthorityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AuthorityMapper implements MapperDataHandler {

    private final AuthorityRepository authorityRepository;

    public AuthorityMapper(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority toAuthority(final AuthorityRequest authorityRequest) {
        Long authorityId = this.getData(authorityRequest.getId(), 0L);
        return this.authorityRepository.findById(authorityId)
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
