package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.dto.FilialDepartmentRequest;
import org.burgas.ticketservice.dto.FilialDepartmentResponse;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.exception.FilialDepartmentNotCreatedException;
import org.burgas.ticketservice.exception.FilialDepartmentNotFoundException;
import org.burgas.ticketservice.mapper.FilialDepartmentMapper;
import org.burgas.ticketservice.repository.FilialDepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.ticketservice.log.FilialDepartmentLogs.*;
import static org.burgas.ticketservice.message.FilialDepartmentMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FilialDepartmentService {

    private static final Logger log = LoggerFactory.getLogger(FilialDepartmentService.class);
    private final FilialDepartmentRepository filialDepartmentRepository;
    private final FilialDepartmentMapper filialDepartmentMapper;
    private final FilialService filialService;
    private final DepartmentService departmentService;

    public FilialDepartmentService(
            FilialDepartmentRepository filialDepartmentRepository, FilialDepartmentMapper filialDepartmentMapper,
            FilialService filialService, DepartmentService departmentService
    ) {
        this.filialDepartmentRepository = filialDepartmentRepository;
        this.filialDepartmentMapper = filialDepartmentMapper;
        this.filialService = filialService;
        this.departmentService = departmentService;
    }

    public List<FilialDepartmentResponse> findAll() {
        return this.filialDepartmentRepository.findAll()
                .stream()
                .peek(filialDepartment -> log.info(FILIAL_DEPARTMENT_FOUND_ALL.getLogMessage(), filialDepartment))
                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<FilialDepartmentResponse>> findAllAsync() {
        return supplyAsync(this.filialDepartmentRepository::findAll)
                .thenApplyAsync(
                        filialDepartments -> filialDepartments.stream()
                                .peek(filialDepartment -> log.info(FILIAL_DEPARTMENT_FOUND_ALL_ASYNC.getLogMessage(), filialDepartment))
                                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                                .collect(Collectors.toList())
                );
    }

    public FilialDepartmentResponse findByFilialIdAndDepartmentId(final String filialId, final String departmentId) {
        return this.filialDepartmentRepository.findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
                .stream()
                .peek(filialDepartment -> log.info(FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID.getLogMessage(), filialDepartment))
                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                .findFirst()
                .orElseGet(FilialDepartmentResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<FilialDepartmentResponse> findByFilialIdAndDepartmentIdAsync(final String filialId, final String departmentId) {
        return supplyAsync(
                () -> this.filialDepartmentRepository.findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
        )
                .thenApplyAsync(
                        filialDepartment -> filialDepartment.stream()
                                .peek(foundFilialDepartment -> log.info(FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID_ASYNC.getLogMessage(), foundFilialDepartment))
                                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                                .findFirst()
                                .orElseGet(FilialDepartmentResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialDepartmentResponse createOrUpdate(final FilialDepartmentRequest filialDepartmentRequest) {
        FilialResponse filialResponse = this.filialService.createOrUpdate(filialDepartmentRequest.getFilial());
        filialDepartmentRequest.getFilial().setId(filialResponse.getId());

        DepartmentResponse departmentResponse = this.departmentService.createOrUpdate(filialDepartmentRequest.getDepartment());
        filialDepartmentRequest.getDepartment().setId(departmentResponse.getId());

        return of(this.filialDepartmentMapper.toFilialDepartment(filialDepartmentRequest))
                .map(this.filialDepartmentRepository::save)
                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                .orElseThrow(
                        () -> new FilialDepartmentNotCreatedException(FILIAL_DEPARTMENT_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<FilialDepartmentResponse> createOrUpdateAsync(final FilialDepartmentRequest filialDepartmentRequest) {
        return supplyAsync(
                () -> {
                    FilialResponse filialResponse = this.filialService.createOrUpdate(filialDepartmentRequest.getFilial());
                    filialDepartmentRequest.getFilial().setId(filialResponse.getId());

                    DepartmentResponse departmentResponse = this.departmentService.createOrUpdate(filialDepartmentRequest.getDepartment());
                    filialDepartmentRequest.getDepartment().setId(departmentResponse.getId());

                    return this.filialDepartmentMapper.toFilialDepartment(filialDepartmentRequest);
                }
        )
                .thenApplyAsync(this.filialDepartmentRepository::save)
                .thenApplyAsync(this.filialDepartmentMapper::toFilialDepartmentResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteFilialDepartment(final String filialId, final String departmentId) {
        return this.filialDepartmentRepository
                .findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
                .map(
                        filialDepartment -> {
                            this.filialDepartmentRepository.deleteFilialDepartmentByFilialIdAndDepartmentId(
                                    filialDepartment.getFilialId(), filialDepartment.getDepartmentId()
                            );
                            return FILIAL_DEPARTMENT_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new FilialDepartmentNotFoundException(FILIAL_DEPARTMENT_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteFilialDepartmentAsync(final String filialId, final String departmentId) {
        return supplyAsync(() -> this.filialDepartmentRepository
                .findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId)))
                .thenApplyAsync(
                        filialDepartment -> filialDepartment.stream()
                                .peek(foundFilialDepartment -> log.info(
                                        FILIAL_DEPARTMENT_FOUND_BY_FILIAL_AND_DEPARTMENT_ID.getLogMessage(), foundFilialDepartment)
                                )
                                .map(
                                        foundFilialDepartment -> {
                                            this.filialDepartmentRepository.deleteFilialDepartmentByFilialIdAndDepartmentId(
                                                    foundFilialDepartment.getFilialId(), foundFilialDepartment.getDepartmentId()
                                            );
                                            return FILIAL_DEPARTMENT_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialDepartmentNotFoundException(FILIAL_DEPARTMENT_NOT_FOUND.getMessage())
                                )
                );
    }
}
