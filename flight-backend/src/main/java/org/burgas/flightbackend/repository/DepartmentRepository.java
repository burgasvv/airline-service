package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Department;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @NotNull Page<Department> findAll(@NotNull Pageable pageable);
}
