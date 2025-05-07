package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Address;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Override
    @NotNull Page<Address> findAll(@NotNull Pageable pageable);
}
