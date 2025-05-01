package org.burgas.flightbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.flightbackend.dto.IdentityRequest;
import org.burgas.flightbackend.dto.IdentityResponse;
import org.burgas.flightbackend.entity.Identity;
import org.burgas.flightbackend.entity.Image;
import org.burgas.flightbackend.exception.*;
import org.burgas.flightbackend.mapper.IdentityMapper;
import org.burgas.flightbackend.message.ImageMessages;
import org.burgas.flightbackend.repository.IdentityRepository;
import org.burgas.flightbackend.repository.RestoreTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.burgas.flightbackend.log.IdentityLogs.*;
import static org.burgas.flightbackend.message.IdentityMessages.*;
import static org.burgas.flightbackend.message.RestoreTokenMessages.WRONG_RESTORE_TOKEN;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

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

    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_ALL.getLogMessage(), identity))
                .map(this.identityMapper::toIdentityResponse)
                .collect(Collectors.toList());
    }

    public IdentityResponse findById(final String identityId) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_ID.getLogMessage(), identity))
                .map(this.identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    public IdentityResponse findByUsername(final String username) {
        return this.identityRepository.findIdentityByUsername(username)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_USERNAME.getLogMessage(), identity))
                .map(this.identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(final IdentityRequest identityRequest) {
        return of(this.identityMapper.toIdentity(identityRequest))
                .map(this.identityRepository::save)
                .map(this.identityMapper::toIdentityResponse)
                .orElseThrow(
                        () -> new IdentityNotCreatedException(IDENTITY_NOT_CREATED.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String accountEnableOrDisable(final String identityId, final String enabled) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            Boolean value = Boolean.valueOf(enabled);
                            if (identity.getEnabled().equals(value))
                                throw new IdentitySameStatusException(IDENTITY_SAME_STATUS.getMessage());

                            identity.setEnabled(value);
                            this.identityRepository.save(identity);
                            return value ? IDENTITY_TURNED_ON.getMessage() :
                                    IDENTITY_TURNED_OFF.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changePassword(final String identityId) {
        return this.customJavaMailSender.sendTokenByEmail(Long.valueOf(identityId));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse setPassword(final String identityId, final String token, final String password) {
        return this.restoreTokenRepository
                .existsRestoreTokenByValueAndIdentityId(UUID.fromString(token), Long.valueOf(identityId))
                .map(
                        _ -> this.identityRepository.findById(Long.valueOf(identityId))
                                .map(
                                        identity -> {
                                            identity.setPassword(this.passwordEncoder.encode(password));
                                            Identity saved = this.identityRepository.save(identity);
                                            this.restoreTokenRepository
                                                    .deleteRestoreTokenByValueAndIdentityId(UUID.fromString(token), Long.valueOf(identityId));
                                            return of(saved)
                                                    .map(this.identityMapper::toIdentityResponse)
                                                    .orElseGet(IdentityResponse::new);
                                        }
                                )
                                .orElseThrow()
                )
                .orElseThrow(
                        () -> new WrongRestoreTokenException(WRONG_RESTORE_TOKEN.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String identityId, final Part part) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .map(
                        identity -> {
                            Image image = this.imageService.uploadImage(part);
                            identity.setImageId(image.getId());
                            this.identityRepository.save(identity);
                            return format(IDENTITY_IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String identityId, final Part part) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .map(
                        identity -> of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(imageId, part);
                                            return format(IDENTITY_IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String identityId) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .map(
                        identity -> Optional.of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(imageId);
                                            return format(IDENTITY_IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }
}