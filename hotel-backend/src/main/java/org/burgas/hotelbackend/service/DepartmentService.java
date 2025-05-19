package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.DepartmentRequest;
import org.burgas.hotelbackend.dto.DepartmentResponse;
import org.burgas.hotelbackend.exception.DepartmentNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.exception.DepartmentNotFoundException;
import org.burgas.hotelbackend.mapper.DepartmentMapper;
import org.burgas.hotelbackend.repository.DepartmentRepository;
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
import static org.burgas.hotelbackend.log.DepartmentLogs.*;
import static org.burgas.hotelbackend.message.DepartmentMessages.*;
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
                                .peek(department -> log.info(DEPARTMENT_FOUND_ALL_ASYNC.getLogMessage(), department))
                                .map(this.departmentMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<DepartmentResponse> findAllPages(final Integer page, final Integer size) {
        return this.departmentRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name"))
                .map(this.departmentMapper::toResponse);
    }

    public List<DepartmentResponse> findByFilialId(final Long filialId) {
        return this.departmentRepository.findDepartmentsByFilialId(filialId == null ? 0L : filialId)
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BY_FILIAL_ID.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<DepartmentResponse>> findByFilialIdAsync(final Long filialId) {
        return supplyAsync(() -> this.departmentRepository.findDepartmentsByFilialId(filialId == null ? 0L : filialId))
                .thenApplyAsync(
                        departments -> departments.stream()
                                .peek(department -> log.info(DEPARTMENT_FOUND_BY_FILIAL_ID_ASYNC.getLogMessage(), department))
                                .map(this.departmentMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public DepartmentResponse findById(final Long departmentId) {
        return this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BY_ID.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .findFirst()
                .orElseGet(DepartmentResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<DepartmentResponse> findByIdAsync(final Long departmentId) {
        return supplyAsync(() -> this.departmentRepository.findById(departmentId == null ? 0L : departmentId))
                .thenApplyAsync(
                        department -> department.stream()
                                .peek(foundDepartment -> log.info(DEPARTMENT_FOUND_BY_ID_ASYNC.getLogMessage(), foundDepartment))
                                .map(this.departmentMapper::toResponse)
                                .findFirst()
                                .orElseGet(DepartmentResponse::new)
                );
    }

    public DepartmentResponse findByName(final String name) {
        return this.departmentRepository.findDepartmentByName(name == null ? "" : name)
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BY_NAME.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .findFirst()
                .orElseGet(DepartmentResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<DepartmentResponse> findByNameAsync(final String name) {
        return supplyAsync(() -> this.departmentRepository.findDepartmentByName(name == null ? "" : name))
                .thenApplyAsync(
                        department -> department.stream()
                                .peek(foundDepartment -> log.info(DEPARTMENT_FOUND_BY_NAME_ASYNC.getLogMessage(), foundDepartment))
                                .map(this.departmentMapper::toResponse)
                                .findFirst()
                                .orElseGet(DepartmentResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public DepartmentResponse createOrUpdate(final DepartmentRequest departmentRequest) {
        return of(this.departmentMapper.toEntity(departmentRequest))
                .map(this.departmentRepository::save)
                .stream()
                .peek(department -> log.info(DEPARTMENT_CREATED_OR_UPDATED.getLogMessage(), department))
                .map(this.departmentMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new DepartmentNotCreatedOrUpdatedException(DEPARTMENT_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<DepartmentResponse> createOrUpdateAsync(final DepartmentRequest departmentRequest) {
        return supplyAsync(() -> this.departmentMapper.toEntity(departmentRequest))
                .thenApplyAsync(this.departmentRepository::save)
                .thenApplyAsync(
                        department -> of(department).stream()
                                .peek(createdOrUpdatedDepartment -> log.info(DEPARTMENT_CREATED_OR_UPDATED_ASYNC.getLogMessage(), createdOrUpdatedDepartment))
                                .map(this.departmentMapper::toResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new DepartmentNotCreatedOrUpdatedException(DEPARTMENT_NOT_CREATED_OR_UPDATED.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long departmentId) {
        return this.departmentRepository.findById(departmentId == null ? 0L : departmentId)
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BEFORE_DELETE.getLogMessage(), department))
                .map(
                        department -> {
                            this.departmentRepository.deleteById(department.getId());
                            return DEPARTMENT_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(() -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long departmentId) {
        return supplyAsync(() -> this.departmentRepository.findById(departmentId == null ? 0L : departmentId))
                .thenApplyAsync(
                        department -> department.stream()
                                .peek(foundDepartment -> log.info(DEPARTMENT_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundDepartment))
                                .map(
                                        foundDepartment -> {
                                            this.departmentRepository.deleteById(foundDepartment.getId());
                                            return DEPARTMENT_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage())
                                )
                );
    }
}
