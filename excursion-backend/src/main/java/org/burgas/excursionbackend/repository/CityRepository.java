package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.City;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Override
    @NotNull
    Page<City> findAll(@NotNull Pageable pageable);
}
