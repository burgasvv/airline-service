package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Country;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findCountryByName(String name);

    @Override
    @NotNull Page<Country> findAll(@NotNull Pageable pageable);
}
