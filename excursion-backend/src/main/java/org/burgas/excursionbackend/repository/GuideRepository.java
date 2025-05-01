package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Guide;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    @Override
    @NotNull
    Page<Guide> findAll(@NotNull Pageable pageable);
}
