package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Department;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Override
    @NotNull Page<Department> findAll(@NotNull Pageable pageable);

    @Query(
            nativeQuery = true,
            value = """
                    select d.* from hotel_backend.public.department d
                        join public.filial_department fd on d.id = fd.department_id
                        where fd.filial_id = :filialId
                    """
    )
    List<Department> findDepartmentsByFilialId(Long filialId);

    Optional<Department> findDepartmentByName(String name);
}
