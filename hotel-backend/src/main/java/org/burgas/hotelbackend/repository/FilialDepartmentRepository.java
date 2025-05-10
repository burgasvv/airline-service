package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.FilialDepartment;
import org.burgas.hotelbackend.entity.FilialDepartmentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialDepartmentRepository extends JpaRepository<FilialDepartment, FilialDepartmentPK> {

    void deleteFilialDepartmentsByFilialId(Long filialId);
}
