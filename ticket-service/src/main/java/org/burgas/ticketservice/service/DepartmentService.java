package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.exception.DepartmentNotCreatedException;
import org.burgas.ticketservice.exception.DepartmentNotFoundException;
import org.burgas.ticketservice.mapper.DepartmentMapper;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.burgas.ticketservice.log.DepartmentLogs.DEPARTMENT_FOUND_ALL;
import static org.burgas.ticketservice.log.DepartmentLogs.DEPARTMENT_FOUND_BY_ID;
import static org.burgas.ticketservice.message.DepartmentMessages.*;
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
                .map(this.departmentMapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse findById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId))
                .stream()
                .peek(department -> log.info(DEPARTMENT_FOUND_BY_ID.getLogMessage(), department))
                .map(this.departmentMapper::toDepartmentResponse)
                .findFirst()
                .orElseGet(DepartmentResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public DepartmentResponse createOrUpdate(final DepartmentRequest departmentRequest) {
        return of(this.departmentMapper.toDepartment(departmentRequest))
                .map(this.departmentRepository::save)
                .map(this.departmentMapper::toDepartmentResponse)
                .orElseThrow(() -> new DepartmentNotCreatedException(DEPARTMENT_NOT_CREATED.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId))
                .map(
                        department -> {
                            this.departmentRepository.deleteById(department.getId());
                            return DEPARTMENT_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new DepartmentNotFoundException(DEPARTMENT_NOT_FOUND.getMessage()));
    }
}
