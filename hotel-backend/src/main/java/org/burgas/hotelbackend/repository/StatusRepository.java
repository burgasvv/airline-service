package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Status;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Override
    @NotNull Page<Status> findAll(@NotNull Pageable pageable);
}
