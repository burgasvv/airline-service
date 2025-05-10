package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.IdentityRequest;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.entity.Identity;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.AuthorityRepository;
import org.burgas.hotelbackend.repository.IdentityRepository;
import org.burgas.hotelbackend.repository.ImageRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class IdentityMapper implements MapperDataHandler<IdentityRequest, Identity, IdentityResponse> {

    private final IdentityRepository identityRepository;
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    public IdentityMapper(
            IdentityRepository identityRepository, AuthorityRepository authorityRepository,
            AuthorityMapper authorityMapper, ImageRepository imageRepository, PasswordEncoder passwordEncoder
    ) {
        this.identityRepository = identityRepository;
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Identity toEntity(IdentityRequest identityRequest) {
        Long identityId = this.getData(identityRequest.getId(), 0L);
        String password = identityRequest.getPassword() == null || identityRequest.getPassword().isBlank() ? "" : identityRequest.getPassword();
        return this.identityRepository.findById(identityId)
                .map(
                        identity -> Identity.builder()
                                .id(identity.getId())
                                .username(this.getData(identityRequest.getUsername(), identity.getUsername()))
                                .password(this.getData(this.passwordEncoder.encode(password), identity.getPassword()))
                                .email(this.getData(identityRequest.getEmail(), identity.getEmail()))
                                .phone(this.getData(identityRequest.getPhone(), identity.getPhone()))
                                .registeredAt(this.getData(identityRequest.getRegisteredAt(), identity.getRegisteredAt()))
                                .enabled(this.getData(identityRequest.getEnabled(), identity.getEnabled()))
                                .authorityId(this.getData(identityRequest.getAuthorityId(), identity.getAuthorityId()))
                                .imageId(this.getData(identityRequest.getImageId(), identity.getImageId()))
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
                                .imageId(identityRequest.getImageId())
                                .build()
                );
    }

    @Override
    public IdentityResponse toResponse(Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .username(identity.getUsername())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .phone(identity.getPhone())
                .registeredAt(identity.getRegisteredAt().format(ofPattern("dd.MM.yyyy, hh:ss")))
                .enabled(identity.getEnabled())
                .authority(
                        this.authorityRepository.findById(identity.getAuthorityId())
                                .map(this.authorityMapper::toResponse)
                                .orElse(null)
                )
                .image(this.imageRepository.findById(identity.getImageId() == null ? 0L : identity.getImageId()).orElse(null))
                .build();
    }
}
