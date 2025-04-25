package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequireAnswerTokenRepository extends JpaRepository<RequireAnswerToken, Long> {

    Optional<RequireAnswerToken> findRequireAnswerTokenByValue(UUID value);
}
