package org.burgas.ticketservice.service;

import org.burgas.ticketservice.entity.Identity;
import org.burgas.ticketservice.entity.RestoreToken;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.burgas.ticketservice.message.IdentityMessage.IDENTITY_TOKEN_WAS_SEND;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CustomJavaMailSender {

    private final IdentityRepository identityRepository;
    private final RestoreTokenService restoreTokenService;
    private final JavaMailSender javaMailSender;

    public CustomJavaMailSender(
            IdentityRepository identityRepository, RestoreTokenService restoreTokenService, JavaMailSender javaMailSender
    ) {
        this.identityRepository = identityRepository;
        this.restoreTokenService = restoreTokenService;
        this.javaMailSender = javaMailSender;
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> sendTokenByEmail(final Long identityId) {
        return Mono.zip(
                this.identityRepository.findById(identityId),
                this.restoreTokenService.createOrUpdateByIdentityId(identityId)
        )
                .flatMap(
                        objects -> {
                            Identity identity = objects.getT1();
                            RestoreToken restoreToken = objects.getT2();
                            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                            simpleMailMessage.setTo(identity.getEmail());
                            simpleMailMessage.setFrom("ADMIN");
                            simpleMailMessage.setSubject("Forgot password? Create the new one");
                            simpleMailMessage.setText("Use this token to create new password: " + restoreToken.getValue());
                            this.javaMailSender.send(simpleMailMessage);
                            return Mono.fromCallable(IDENTITY_TOKEN_WAS_SEND::getMessage);
                        }
                );
    }
}
