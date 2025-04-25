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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static org.burgas.ticketservice.message.IdentityMessage.*;
import static org.burgas.ticketservice.message.RestoreTokenMessage.WRONG_RESTORE_TOKEN;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final CustomJavaMailSender customJavaMailSender;
    private final RestoreTokenRepository restoreTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public IdentityService(
            IdentityRepository identityRepository, IdentityMapper identityMapper,
            CustomJavaMailSender customJavaMailSender, RestoreTokenRepository restoreTokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.customJavaMailSender = customJavaMailSender;
        this.restoreTokenRepository = restoreTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .stream()
                .map(this.identityMapper::toIdentityResponse)
                .toList();
    }

    public IdentityResponse findById(final String identityId) {
        return this.identityRepository.findById(Long.parseLong(identityId))
                .map(this.identityMapper::toIdentityResponse)
                .orElseGet(IdentityResponse::new);
    }

    public IdentityResponse findByUsername(final String username) {
        return this.identityRepository.findIdentityByUsername(username)
                .map(this.identityMapper::toIdentityResponse)
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
                .orElseGet(IdentityResponse::new);
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
                                            return Optional.of(saved)
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
}