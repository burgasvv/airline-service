package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Filial;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    @Override
    @NotNull Page<Filial> findAll(@NotNull Pageable pageable);
}
