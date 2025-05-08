package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Hotel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findHotelByName(String name);

    @Override
    @NotNull Page<Hotel> findAll(@NotNull Pageable pageable);
}
