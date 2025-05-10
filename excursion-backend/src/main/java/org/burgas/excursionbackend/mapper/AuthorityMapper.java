package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.AuthorityRequest;
import org.burgas.excursionbackend.dto.AuthorityResponse;
import org.burgas.excursionbackend.entity.Authority;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.AuthorityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AuthorityMapper implements MapperDataHandler<AuthorityRequest, Authority, AuthorityResponse> {

    private final AuthorityRepository authorityRepository;

    public AuthorityMapper(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority toEntity(AuthorityRequest authorityRequest) {
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

    @Override
    public AuthorityResponse toResponse(Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .name(authority.getName())
                .build();
    }
}
