package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.FilialDepartment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FilialDepartmentRepository extends R2dbcRepository<FilialDepartment, Long> {

    Mono<FilialDepartment> findFilialDepartmentByFilialIdAndDepartmentId(Long filialId, Long departmentId);

    Mono<Void> deleteFilialDepartmentByFilialIdAndDepartmentId(Long filialId, Long departmentId);
}
