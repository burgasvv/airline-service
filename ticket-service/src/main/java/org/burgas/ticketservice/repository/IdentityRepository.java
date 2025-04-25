package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    Optional<Identity> findIdentityByUsername(String username);
}
