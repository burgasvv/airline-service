package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.dto.RequireRequest;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.entity.Require;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.stereotype.Component;

@Component
public final class RequireMapper {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public RequireMapper(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    public Require toRequire(final RequireRequest requireRequest) {
        return Require.builder()
                .name(requireRequest.getName())
                .surname(requireRequest.getSurname())
                .patronymic(requireRequest.getPatronymic())
                .passport(requireRequest.getPassport())
                .adminId(requireRequest.getAdminId())
                .userId(requireRequest.getUserId())
                .closed(false)
                .build();
    }

    public RequireResponse toRequireResponse(final Require require) {
        return RequireResponse.builder()
                .id(require.getId())
                .name(require.getName())
                .surname(require.getSurname())
                .patronymic(require.getPatronymic())
                .passport(require.getPassport())
                .closed(require.getClosed())
                .admin(
                        this.identityRepository.findById(require.getAdminId())
                                .map(this.identityMapper::toIdentityResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .user(
                        this.identityRepository.findById(require.getUserId())
                                .map(this.identityMapper::toIdentityResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .build();
    }
}
