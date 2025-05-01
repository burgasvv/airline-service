package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Employee;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @NotNull Page<Employee> findAll(@NotNull Pageable pageable);
}
