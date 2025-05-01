package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Address;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @NotNull Page<Address> findAll(@NotNull Pageable pageable);
}
