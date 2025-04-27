package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.dto.AuthorityResponse;
import org.burgas.ticketservice.exception.AuthorityNotCreatedException;
import org.burgas.ticketservice.exception.AuthorityNotFoundException;
import org.burgas.ticketservice.mapper.AuthorityMapper;
import org.burgas.ticketservice.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.of;
import static org.burgas.ticketservice.log.AuthorityLogs.*;
import static org.burgas.ticketservice.message.AuthorityMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AuthorityService {

    private static final Logger log = LoggerFactory.getLogger(AuthorityService.class);
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    public List<AuthorityResponse> findAll() {
        return this.authorityRepository.findAll()
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_ALL.getLogMessage(), authority))
                .map(this.authorityMapper::toAuthorityResponse)
                .toList();
    }

    public AuthorityResponse findById(final String authorityId) {
        return this.authorityRepository.findById(Long.valueOf(authorityId))
                .stream()
                .peek(authority -> log.info(AUTHORITY_FOUND_BY_ID.getLogMessage(), authority))
                .map(this.authorityMapper::toAuthorityResponse)
                .findFirst()
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
                .orElseThrow(() -> new AuthorityNotCreatedException(AUTHORITY_NOT_CREATED.getMessage()));
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
                            log.info(AUTHORITY_DELETED_BY_ID.getLogMessage());
                            return AUTHORITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NOT_FOUND.getMessage()));
    }
}
