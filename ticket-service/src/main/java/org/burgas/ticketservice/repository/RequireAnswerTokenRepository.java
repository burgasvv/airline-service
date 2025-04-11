package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RequireAnswerTokenRepository extends R2dbcRepository<RequireAnswerToken, Long> {

    Mono<RequireAnswerToken> findRequireAnswerTokenByValue(UUID value);
}
