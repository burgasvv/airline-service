package org.burgas.ticketservice.service;

import org.burgas.ticketservice.mapper.IdentityMapper;
import org.burgas.ticketservice.repository.IdentityRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public CustomUserDetailsService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.identityRepository.findIdentityByEmail(username)
                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity)));
    }
}
