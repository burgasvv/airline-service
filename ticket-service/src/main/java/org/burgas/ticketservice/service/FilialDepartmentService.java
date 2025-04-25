package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.dto.FilialDepartmentRequest;
import org.burgas.ticketservice.dto.FilialDepartmentResponse;
import org.burgas.ticketservice.exception.FilialDepartmentNotFoundException;
import org.burgas.ticketservice.mapper.FilialDepartmentMapper;
import org.burgas.ticketservice.repository.FilialDepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.burgas.ticketservice.message.FilialDepartmentMessage.FILIAL_DEPARTMENT_DELETED;
import static org.burgas.ticketservice.message.FilialDepartmentMessage.FILIAL_DEPARTMENT_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FilialDepartmentService {

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
                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                .toList();
    }

    public FilialDepartmentResponse findByFilialIdAndDepartmentId(final String filialId, final String departmentId) {
        return this.filialDepartmentRepository.findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
                .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                .orElseGet(FilialDepartmentResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialDepartmentResponse createOrUpdate(final FilialDepartmentRequest filialDepartmentRequest) {
        return Optional.of(this.filialService.createOrUpdate(filialDepartmentRequest.getFilial()))
                .map(
                        filialResponse -> {
                            filialDepartmentRequest.getFilial().setId(filialResponse.getId());
                            DepartmentResponse departmentResponse = this.departmentService.createOrUpdate(filialDepartmentRequest.getDepartment());
                            filialDepartmentRequest.getDepartment().setId(departmentResponse.getId());
                            return Optional.of(this.filialDepartmentMapper.toFilialDepartment(filialDepartmentRequest))
                                    .map(this.filialDepartmentRepository::save)
                                    .map(this.filialDepartmentMapper::toFilialDepartmentResponse)
                                    .orElseGet(FilialDepartmentResponse::new);
                        }
                )
                .orElseThrow();
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
}
