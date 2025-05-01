package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Country;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Override
    @NotNull
    Page<Country> findAll(@NotNull Pageable pageable);
}
