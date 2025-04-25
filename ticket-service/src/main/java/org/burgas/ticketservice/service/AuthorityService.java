package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.dto.AuthorityResponse;
import org.burgas.ticketservice.exception.AuthorityNotFoundException;
import org.burgas.ticketservice.mapper.AuthorityMapper;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.of;
import static org.burgas.ticketservice.message.AuthorityMessage.AUTHORITY_DELETED;
import static org.burgas.ticketservice.message.AuthorityMessage.AUTHORITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    public List<AuthorityResponse> findAll() {
        return this.authorityRepository.findAll()
                .stream()
                .map(this.authorityMapper::toAuthorityResponse)
                .toList();
    }

    public AuthorityResponse findById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId))
                .map(this.authorityMapper::toAuthorityResponse)
                .orElseGet(AuthorityResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(final AuthorityRequest authorityRequest) {
        return of(this.authorityMapper.toAuthority(authorityRequest))
                .map(this.authorityRepository::save)
                .map(this.authorityMapper::toAuthorityResponse)
                .map(AuthorityResponse::getId)
                .orElse(null);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId))
                .map(
                        authority -> {
                            this.authorityRepository.deleteById(authority.getId());
                            return AUTHORITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()));
    }
}
