package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.IdentityResponse;
import org.burgas.flightbackend.dto.RequireRequest;
import org.burgas.flightbackend.dto.RequireResponse;
import org.burgas.flightbackend.entity.Require;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.IdentityRepository;
import org.springframework.stereotype.Component;

@Component
public final class RequireMapper implements MapperDataHandler<RequireRequest, Require, RequireResponse> {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public RequireMapper(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public Require toEntity(RequireRequest requireRequest) {
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

    @Override
    public RequireResponse toResponse(Require require) {
        return RequireResponse.builder()
                .id(require.getId())
                .name(require.getName())
                .surname(require.getSurname())
                .patronymic(require.getPatronymic())
                .passport(require.getPassport())
                .closed(require.getClosed())
                .admin(
                        this.identityRepository.findById(require.getAdminId())
                                .map(this.identityMapper::toResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .user(
                        this.identityRepository.findById(require.getUserId())
                                .map(this.identityMapper::toResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .build();
    }
}
