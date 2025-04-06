package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.IdentityRequest;
import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.entity.Identity;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class IdentityMapper {

    private final IdentityRepository identityRepository;
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;
    private final PasswordEncoder passwordEncoder;

    public IdentityMapper(
            IdentityRepository identityRepository, AuthorityRepository authorityRepository,
                          AuthorityMapper authorityMapper, PasswordEncoder passwordEncoder
    ) {
        this.identityRepository = identityRepository;
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    public Mono<Identity> toIdentity(final Mono<IdentityRequest> identityRequestMono) {
        return identityRequestMono
                .flatMap(
                        identityRequest -> {
                            Long identityId = this.getData(identityRequest.getId(), 0L);
                            return this.identityRepository
                                    .findById(identityId)
                                    .flatMap(
                                            identity -> Mono.fromCallable(
                                                    () -> Identity.builder()
                                                            .id(identity.getId())
                                                            .authorityId(this.getData(identityRequest.getAuthorityId(), identity.getAuthorityId()))
                                                            .username(this.getData(identityRequest.getUsername(), identity.getUsername()))
                                                            .password(identity.getPassword())
                                                            .email(this.getData(identityRequest.getEmail(), identity.getEmail()))
                                                            .phone(this.getData(identityRequest.getPhone(), identity.getPhone()))
                                                            .enabled(this.getData(identityRequest.getEnabled(), identity.getEnabled()))
                                                            .registeredAt(identity.getRegisteredAt())
                                                            .isNew(false)
                                                            .build()
                                            )
                                    )
                                    .switchIfEmpty(
                                            Mono.fromCallable(
                                                    () -> Identity.builder()
                                                            .username(identityRequest.getUsername())
                                                            .authorityId(3L)
                                                            .password(this.passwordEncoder.encode(identityRequest.getPassword()))
                                                            .email(identityRequest.getEmail())
                                                            .phone(identityRequest.getPhone())
                                                            .registeredAt(LocalDateTime.now())
                                                            .enabled(true)
                                                            .isNew(true)
                                                            .build()
                                            )
                                    );
                        }
                );
    }

    public Mono<IdentityResponse> toIdentityResponse(final Mono<Identity> identityMono) {
        return identityMono
                .flatMap(
                        identity -> this.authorityRepository
                                .findById(identity.getAuthorityId())
                                .flatMap(
                                        authority -> this.authorityMapper
                                                .toAuthorityResponse(Mono.fromCallable(() -> authority))
                                                .flatMap(
                                                        authorityResponse -> Mono.fromCallable(
                                                                () -> IdentityResponse.builder()
                                                                        .id(identity.getId())
                                                                        .authority(authorityResponse)
                                                                        .username(identity.getUsername())
                                                                        .password(identity.getPassword())
                                                                        .email(identity.getEmail())
                                                                        .phone(identity.getPhone())
                                                                        .registeredAt(
                                                                                identity.getRegisteredAt()
                                                                                        .format(ofPattern("dd.MM.yyyy, hh:mm:ss"))
                                                                        )
                                                                        .enabled(identity.getEnabled())
                                                                        .imageId(identity.getImageId())
                                                                        .build()
                                                        )
                                                )
                                )
                );
    }
}
