package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.RequireRequest;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.entity.Require;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class RequireMapper {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public RequireMapper(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    public Mono<Require> toRequire(final Mono<RequireRequest> requireRequestMono) {
        return requireRequestMono.flatMap(
                requireRequest -> Mono.fromCallable(() ->
                        Require.builder()
                                .name(requireRequest.getName())
                                .surname(requireRequest.getSurname())
                                .patronymic(requireRequest.getPatronymic())
                                .passport(requireRequest.getPassport())
                                .adminId(requireRequest.getAdminId())
                                .userId(requireRequest.getUserId())
                                .closed(false)
                                .isNew(true)
                                .build())
        );
    }

    public Mono<RequireResponse> toRequireResponse(final Mono<Require> requireMono) {
        return requireMono.flatMap(
                require -> Mono.zip(
                        this.identityRepository.findById(require.getAdminId())
                                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity))),
                        this.identityRepository.findById(require.getUserId())
                                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity)))
                )
                        .flatMap(
                                objects -> Mono.fromCallable(() ->
                                        RequireResponse.builder()
                                                .id(require.getId())
                                                .name(require.getName())
                                                .surname(require.getSurname())
                                                .patronymic(require.getPatronymic())
                                                .passport(require.getPassport())
                                                .closed(require.getClosed())
                                                .admin(objects.getT1())
                                                .user(objects.getT2())
                                                .build()
                                )
                        )
        );
    }
}
