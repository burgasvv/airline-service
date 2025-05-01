package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.FilialDepartment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilialDepartmentRepository extends JpaRepository<FilialDepartment, Long> {

    Optional<FilialDepartment> findFilialDepartmentByFilialIdAndDepartmentId(Long filialId, Long departmentId);

    void deleteFilialDepartmentByFilialIdAndDepartmentId(Long filialId, Long departmentId);

    @NotNull Page<FilialDepartment> findAll(@NotNull Pageable pageable);
}
