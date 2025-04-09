package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.exception.DepartmentNotFoundException;
import org.burgas.ticketservice.mapper.DepartmentMapper;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static org.burgas.ticketservice.message.DepartmentMessage.DEPARTMENT_DELETED;
import static org.burgas.ticketservice.message.DepartmentMessage.DEPARTMENT_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public Flux<DepartmentResponse> findAll() {
        return this.departmentRepository.findAll()
                .flatMap(department -> this.departmentMapper.toDepartmentResponse(Mono.fromCallable(() -> department)));
    }

    public Mono<DepartmentResponse> findById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId))
                .flatMap(department -> this.departmentMapper.toDepartmentResponse(Mono.fromCallable(() -> department)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<DepartmentResponse> createOrUpdate(final Mono<DepartmentRequest> departmentRequestMono) {
        return departmentRequestMono.flatMap(
                departmentRequest -> this.departmentMapper.toDepartment(Mono.fromCallable(() -> departmentRequest))
                        .flatMap(this.departmentRepository::save)
                        .flatMap(department -> this.departmentMapper.toDepartmentResponse(Mono.fromCallable(() -> department)))
        );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId))
                .flatMap(
                        department -> this.departmentRepository.deleteById(requireNonNull(department.getId()))
                                .thenReturn(DEPARTMENT_DELETED.getMessage())
                )
                .switchIfEmpty(
                        Mono.error(new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage()))
                );
    }
}
