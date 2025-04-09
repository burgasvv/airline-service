package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.IdentityRequest;
import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.entity.Identity;
import org.burgas.ticketservice.exception.IdentityNotFoundException;
import org.burgas.ticketservice.exception.IdentitySameStatusException;
import org.burgas.ticketservice.exception.WrongRestoreTokenException;
import org.burgas.ticketservice.mapper.IdentityMapper;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.burgas.ticketservice.repository.RestoreTokenRepository;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.UUID;
import java.util.logging.Level;

import static org.burgas.ticketservice.message.IdentityMessage.*;
import static org.burgas.ticketservice.message.ImageMessage.IDENTITY_IMAGE_CHANGED;
import static org.burgas.ticketservice.message.ImageMessage.IDENTITY_IMAGE_UPLOADED;
import static org.burgas.ticketservice.message.RestoreTokenMessage.WRONG_RESTORE_TOKEN;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    public static final String FIND_IDENTITY = "Find identity";
    public static final String TRANSFORM_TO_RESPONSE = "Transform to identity response";

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final CustomJavaMailSender customJavaMailSender;
    private final RestoreTokenRepository restoreTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public IdentityService(
            IdentityRepository identityRepository, IdentityMapper identityMapper,
            CustomJavaMailSender customJavaMailSender, RestoreTokenRepository restoreTokenRepository,
            PasswordEncoder passwordEncoder, ImageService imageService
    ) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.customJavaMailSender = customJavaMailSender;
        this.restoreTokenRepository = restoreTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    public Flux<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .log(FIND_IDENTITY, Level.INFO, SignalType.ON_NEXT)
                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity)))
                .log(TRANSFORM_TO_RESPONSE, Level.FINE, SignalType.ON_COMPLETE);
    }

    public Mono<IdentityResponse> findById(final String identityId) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .log(FIND_IDENTITY, Level.INFO, SignalType.ON_NEXT)
                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity)))
                .log(TRANSFORM_TO_RESPONSE, Level.FINE, SignalType.ON_COMPLETE);
    }

    public Mono<IdentityResponse> findByUsername(final String username) {
        return this.identityRepository
                .findIdentityByUsername(username)
                .log(FIND_IDENTITY, Level.INFO, SignalType.ON_NEXT)
                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity)))
                .log(TRANSFORM_TO_RESPONSE, Level.FINE, SignalType.ON_COMPLETE);
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<IdentityResponse> createOrUpdate(final Mono<IdentityRequest> identityRequestMono) {
        return identityRequestMono
                .flatMap(
                        identityRequest ->
                                this.identityMapper.toIdentity(Mono.fromCallable(() -> identityRequest))
                                        .flatMap(this.identityRepository::save)
                                        .flatMap(
                                                identity -> this.identityMapper.toIdentityResponse(
                                                        Mono.fromCallable(() -> identity)
                                                )
                                        )
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> accountEnableOrDisable(final String identityId, final String enabled) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .flatMap(
                        identity -> {
                            Boolean value = Boolean.valueOf(enabled);
                            if (identity.getEnabled().equals(value))
                                return Mono.error(new IdentitySameStatusException(IDENTITY_SAME_STATUS.getMessage()));

                            identity.setEnabled(value);
                            identity.setNew(false);
                            return this.identityRepository.save(identity)
                                    .then(
                                            Mono.fromCallable(
                                                    () -> value ? IDENTITY_TURNED_ON.getMessage() :
                                                            IDENTITY_TURNED_OFF.getMessage()
                                            )
                                    );
                        }
                )
                .switchIfEmpty(
                        Mono.error(new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> changePassword(final String identityId) {
        return this.customJavaMailSender.sendTokenByEmail(Long.valueOf(identityId));
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<IdentityResponse> setPassword(final String identityId, final String token, final String password) {
        return this.restoreTokenRepository
                .existsRestoreTokenByValueAndIdentityId(UUID.fromString(token), Long.valueOf(identityId))
                .flatMap(
                        _ -> this.identityRepository
                                .findById(Long.valueOf(identityId))
                                .flatMap(
                                        identity -> {
                                            identity.setPassword(this.passwordEncoder.encode(password));
                                            identity.setNew(false);
                                            Mono<Identity> saved = this.identityRepository.save(identity);
                                            return this.restoreTokenRepository
                                                    .deleteRestoreTokenByValueAndIdentityId(UUID.fromString(token), Long.valueOf(identityId))
                                                    .then(
                                                            saved.flatMap(
                                                            iden -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> iden))
                                                            )
                                                    );
                                        }
                                )
                )
                .switchIfEmpty(
                        Mono.error(new WrongRestoreTokenException(WRONG_RESTORE_TOKEN.getMessage()))
                );
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> uploadIdentityImage(final String identityId, final FilePart filePart) {
        return this.identityRepository
                .findById(Long.valueOf(identityId))
                .flatMap(
                        identity -> this.imageService.uploadImage(filePart)
                                .flatMap(
                                        image -> {
                                            identity.setImageId(image.getId());
                                            identity.setNew(false);
                                            return this.identityRepository.save(identity)
                                                    .then(Mono.fromCallable(IDENTITY_IMAGE_UPLOADED::getMessage));
                                        }
                                )
                )
                .switchIfEmpty(
                        Mono.error(new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> changeIdentityImage(final String identityId, final FilePart filePart) {
        return this.identityRepository
                .findById(Long.valueOf(identityId))
                .flatMap(
                        identity -> this.imageService.changeImage(identity.getImageId(), filePart)
                                .then(Mono.fromCallable(IDENTITY_IMAGE_CHANGED::getMessage))
                )
                .switchIfEmpty(
                        Mono.fromCallable(IDENTITY_NOT_FOUND::getMessage)
                );
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteImage(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .flatMap(identity -> this.imageService.deleteImage(identity.getImageId()))
                .switchIfEmpty(
                        Mono.fromCallable(IDENTITY_NOT_FOUND::getMessage)
                );
    }
}