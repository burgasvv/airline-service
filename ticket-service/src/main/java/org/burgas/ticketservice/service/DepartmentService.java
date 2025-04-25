package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.exception.DepartmentNotFoundException;
import org.burgas.ticketservice.mapper.DepartmentMapper;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<DepartmentResponse> findAll() {
        return this.departmentRepository.findAll()
                .stream()
                .map(this.departmentMapper::toDepartmentResponse)
                .toList();
    }

    public DepartmentResponse findById(final String departmentId) {
        return this.departmentRepository.findById(Long.valueOf(departmentId))
                .map(this.departmentMapper::toDepartmentResponse)
                .orElseGet(DepartmentResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public DepartmentResponse createOrUpdate(final DepartmentRequest departmentRequest) {
        return Optional.of(this.departmentMapper.toDepartment(departmentRequest))
                .map(this.departmentRepository::save)
                .map(this.departmentMapper::toDepartmentResponse)
                .orElseGet(DepartmentResponse::new);
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
