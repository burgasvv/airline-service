package org.burgas.ticketservice.service;

import org.burgas.ticketservice.entity.Identity;
import org.burgas.ticketservice.entity.RestoreToken;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.burgas.ticketservice.message.IdentityMessages.IDENTITY_TOKEN_WAS_SEND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
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
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String sendTokenByEmail(final Long identityId) {
        Identity identity = this.identityRepository.findById(identityId).orElseThrow();
        RestoreToken restoreToken = this.restoreTokenService.createOrUpdateByIdentityId(identityId);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(identity.getEmail());
        simpleMailMessage.setFrom("ADMIN");
        simpleMailMessage.setSubject("Forgot password? Create the new one");
        simpleMailMessage.setText("Use this token to create new password: " + restoreToken.getValue());
        this.javaMailSender.send(simpleMailMessage);
        return IDENTITY_TOKEN_WAS_SEND.getMessage();
    }
}
