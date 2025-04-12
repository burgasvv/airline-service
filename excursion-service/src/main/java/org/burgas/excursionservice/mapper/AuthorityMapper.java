package org.burgas.excursionservice.mapper;

import org.burgas.excursionservice.dto.AuthorityRequest;
import org.burgas.excursionservice.dto.AuthorityResponse;
import org.burgas.excursionservice.entity.Authority;
import org.burgas.excursionservice.repository.AuthorityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AuthorityMapper {

    private final AuthorityRepository authorityRepository;

    public AuthorityMapper(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    private <T> T getData(final T first, final T second) {
        return first == null || first == "" ? second : first;
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
