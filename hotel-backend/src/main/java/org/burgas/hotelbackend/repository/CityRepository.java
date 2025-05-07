package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.City;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByName(String name);

    @Override
    @NotNull Page<City> findAll(@NotNull Pageable pageable);
}
