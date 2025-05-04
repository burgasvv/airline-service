package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Identity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    @Override
    @NotNull Page<Identity> findAll(@NotNull Pageable pageable);
}
