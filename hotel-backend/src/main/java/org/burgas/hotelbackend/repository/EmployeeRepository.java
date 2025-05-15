package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Employee;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @NotNull Page<Employee> findAll(@NotNull Pageable pageable);
}
