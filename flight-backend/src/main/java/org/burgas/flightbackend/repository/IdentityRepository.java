package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    Optional<Identity> findIdentityByUsername(String username);
}
