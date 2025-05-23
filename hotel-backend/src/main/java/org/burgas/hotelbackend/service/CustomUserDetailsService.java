package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.mapper.IdentityMapper;
import org.burgas.hotelbackend.repository.IdentityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.burgas.hotelbackend.message.IdentityMessages.IDENTITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
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
                .orElseThrow(() -> new ImageNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }
}
