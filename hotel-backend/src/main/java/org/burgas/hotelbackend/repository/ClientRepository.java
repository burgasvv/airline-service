package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Client;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Override
    @NotNull Page<Client> findAll(@NotNull Pageable pageable);
}
