package org.burgas.excursionservice.mapper;

import org.burgas.excursionservice.dto.AuthorityResponse;
import org.burgas.excursionservice.dto.IdentityRequest;
import org.burgas.excursionservice.dto.IdentityResponse;
import org.burgas.excursionservice.entity.Identity;
import org.burgas.excursionservice.handler.MapperDataHandler;
import org.burgas.excursionservice.repository.AuthorityRepository;
import org.burgas.excursionservice.repository.IdentityRepository;
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
        String password = identityRequest.getPassword() == null ? "" : identityRequest.getPassword();
        return this.identityRepository.findById(identityId)
                .map(
                        identity -> Identity.builder()
                                .id(identity.getId())
                                .username(this.getData(identityRequest.getUsername(), identity.getUsername()))
                                .password(this.getData(identityRequest.getPassword(), identity.getPassword()))
                                .email(this.getData(identityRequest.getEmail(), identity.getEmail()))
                                .phone(this.getData(identityRequest.getPhone(), identity.getPhone()))
                                .registeredAt(identity.getRegisteredAt())
                                .enabled(this.getData(identityRequest.getEnabled(), identity.getEnabled()))
                                .authorityId(this.getData(identityRequest.getAuthorityId(), identity.getAuthorityId()))
                                .build()
                )
                .orElseGet(
                        () -> Identity.builder()
                                .username(identityRequest.getUsername())
                                .password(this.passwordEncoder.encode(password))
                                .email(identityRequest.getEmail())
                                .phone(identityRequest.getPhone())
                                .registeredAt(LocalDateTime.now())
                                .enabled(true)
                                .authorityId(identityRequest.getAuthorityId())
                                .build()
                );
    }

    public IdentityResponse toIdentityResponse(final Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .username(identity.getUsername())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .phone(identity.getPhone())
                .enabled(identity.getEnabled())
                .registeredAt(identity.getRegisteredAt().format(ofPattern("dd.MM.yyyy, hh:mm")))
                .authority(
                        this.authorityRepository.findById(identity.getAuthorityId())
                                .map(this.authorityMapper::toAuthorityResponse)
                                .orElseGet(AuthorityResponse::new)
                )
                .build();
    }
}
