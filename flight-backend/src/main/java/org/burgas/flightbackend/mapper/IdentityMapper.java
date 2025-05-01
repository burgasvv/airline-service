package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.AuthorityResponse;
import org.burgas.flightbackend.dto.IdentityRequest;
import org.burgas.flightbackend.dto.IdentityResponse;
import org.burgas.flightbackend.entity.Identity;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.AuthorityRepository;
import org.burgas.flightbackend.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class IdentityMapper implements MapperDataHandler {

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

    public Identity toIdentity(final IdentityRequest identityRequest) {
        Long identityId = this.getData(identityRequest.getId(), 0L);
        return this.identityRepository
                .findById(identityId)
                .map(
                        identity -> Identity.builder()
                                .id(identity.getId())
                                .authorityId(this.getData(identityRequest.getAuthorityId(), identity.getAuthorityId()))
                                .username(this.getData(identityRequest.getUsername(), identity.getUsername()))
                                .password(identity.getPassword())
                                .email(this.getData(identityRequest.getEmail(), identity.getEmail()))
                                .phone(this.getData(identityRequest.getPhone(), identity.getPhone()))
                                .enabled(this.getData(identityRequest.getEnabled(), identity.getEnabled()))
                                .registeredAt(identity.getRegisteredAt())
                                .build()
                )
                .orElseGet(
                        () -> Identity.builder()
                                .username(identityRequest.getUsername())
                                .authorityId(3L)
                                .password(this.passwordEncoder.encode(identityRequest.getPassword()))
                                .email(identityRequest.getEmail())
                                .phone(identityRequest.getPhone())
                                .registeredAt(LocalDateTime.now())
                                .enabled(true)
                                .build()
                );
    }

    public IdentityResponse toIdentityResponse(final Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .authority(
                        this.authorityRepository.findById(identity.getAuthorityId())
                                .map(this.authorityMapper::toAuthorityResponse)
                                .orElseGet(AuthorityResponse::new)
                )
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
                .build();
    }
}
