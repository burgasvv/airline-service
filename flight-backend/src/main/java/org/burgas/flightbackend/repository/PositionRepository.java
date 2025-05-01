package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Position;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @NotNull Page<Position> findAll(@NotNull Pageable pageable);
}
