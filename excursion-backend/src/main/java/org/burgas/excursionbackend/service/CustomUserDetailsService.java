package org.burgas.excursionbackend.service;

import org.burgas.excursionbackend.exception.IdentityNotFoundException;
import org.burgas.excursionbackend.mapper.IdentityMapper;
import org.burgas.excursionbackend.repository.IdentityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.burgas.excursionbackend.message.IdentityMessages.IDENTITY_NOT_FOUND;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public CustomUserDetailsService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.identityRepository.findIdentityByEmail(username)
                .map(this.identityMapper::toResponse)
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }
}
