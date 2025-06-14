package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.DepartmentRequest;
import org.burgas.flightbackend.dto.DepartmentResponse;
import org.burgas.flightbackend.exception.DepartmentNotCreatedException;
import org.burgas.flightbackend.exception.DepartmentNotFoundException;
import org.burgas.flightbackend.log.DepartmentLogs;
import org.burgas.flightbackend.mapper.DepartmentMapper;
import org.burgas.flightbackend.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.flightbackend.log.DepartmentLogs.*;
import static org.burgas.flightbackend.message.DepartmentMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentResponse> findAll() {
        return this.departmentRepository.findAll()
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_ALL.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<DepartmentResponse>> findAllAsync() {
        return supplyAsync(this.departmentRepository::findAll)
                .thenApplyAsync(
                        departments -> departments.stream()
                                .peek(department -> log.info(DepartmentLogs.DEPARTMENT_FOUND_ALL_ASYNC.getLogMessage(), department))
                                .map(this.departmentMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<DepartmentResponse> findAllPages(final Integer page, final Integer size) {
        return this.departmentRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name"))
                .map(this.departmentMapper::toResponse);
    }

    public DepartmentResponse findById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId == null ? "0" : departmentId))
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BY_ID.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .findFirst()
                .orElseGet(DepartmentResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<DepartmentResponse> findByIdAsync(final String departmentId) {
        return supplyAsync(() -> this.departmentRepository.findById(Long.parseLong(departmentId == null ? "0" : departmentId)))
                .thenApplyAsync(
                        department -> department.stream()
                                .peek(foundDepartment -> log.info(DEPARTMENT_FOUND_BY_ID_ASYNC.getLogMessage(), foundDepartment))
                                .map(this.departmentMapper::toResponse)
                                .findFirst()
                                .orElseGet(DepartmentResponse::new)
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public DepartmentResponse createOrUpdate(final DepartmentRequest departmentRequest) {
        return of(this.departmentMapper.toEntity(departmentRequest))
                .map(this.departmentRepository::save)
                .map(this.departmentMapper::toResponse)
                .orElseThrow(() -> new DepartmentNotCreatedException(DEPARTMENT_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<DepartmentResponse> createOrUpdateAsync(final DepartmentRequest departmentRequest) {
        return supplyAsync(() -> this.departmentMapper.toEntity(departmentRequest))
                .thenApplyAsync(this.departmentRepository::save)
                .thenApplyAsync(this.departmentMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId == null ? "0" : departmentId))
                .map(
                        department -> {
                            this.departmentRepository.deleteById(department.getId());
                            return DEPARTMENT_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String departmentId) {
        return supplyAsync(() -> this.departmentRepository.findById(Long.parseLong(departmentId == null ? "0" : departmentId)))
                .thenApplyAsync(
                        department -> department.stream()
                                .peek(foundDepartment -> log.info(DEPARTMENT_FOUND_BEFORE_DELETE.getLogMessage(), foundDepartment))
                                .map(
                                        foundDepartment -> {
                                            this.departmentRepository.deleteById(foundDepartment.getId());
                                            return DEPARTMENT_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                                )
                );
    }
}
