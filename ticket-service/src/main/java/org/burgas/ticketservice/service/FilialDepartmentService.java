package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FilialDepartmentRequest;
import org.burgas.ticketservice.dto.FilialDepartmentResponse;
import org.burgas.ticketservice.exception.FilialDepartmentNotFoundException;
import org.burgas.ticketservice.mapper.FilialDepartmentMapper;
import org.burgas.ticketservice.repository.FilialDepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<FilialDepartmentResponse> findAll() {
        return this.filialDepartmentRepository.findAll()
                .flatMap(filialDepartment -> this.filialDepartmentMapper
                        .toFilialDepartmentResponse(Mono.fromCallable(() -> filialDepartment))
                );
    }

    public Mono<FilialDepartmentResponse> findByFilialIdAndDepartmentId(final String filialId, final String departmentId) {
        return this.filialDepartmentRepository.findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
                .flatMap(filialDepartment -> this.filialDepartmentMapper
                        .toFilialDepartmentResponse(Mono.fromCallable(() -> filialDepartment))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<FilialDepartmentResponse> createOrUpdate(final Mono<FilialDepartmentRequest> filialDepartmentRequestMono) {
        return filialDepartmentRequestMono.flatMap(
                filialDepartmentRequest -> this.filialService.createOrUpdate(
                        Mono.fromCallable(filialDepartmentRequest::getFilial))
                        .flatMap(
                                filialResponse -> {
                                    filialDepartmentRequest.getFilial().setId(filialResponse.getId());
                                    return this.departmentService.createOrUpdate(Mono.fromCallable(filialDepartmentRequest::getDepartment));
                                }
                        )
                        .flatMap(
                                departmentResponse -> {
                                    filialDepartmentRequest.getDepartment().setId(departmentResponse.getId());
                                    return this.filialDepartmentMapper.toFilialDepartment(Mono.fromCallable(() -> filialDepartmentRequest))
                                            .flatMap(this.filialDepartmentRepository::save)
                                            .flatMap(filialDepartment -> this.filialDepartmentMapper
                                                    .toFilialDepartmentResponse(Mono.fromCallable(() -> filialDepartment))
                                            );
                                }
                        )
        );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteFilialDepartment(final String filialId, final String departmentId) {
        return this.filialDepartmentRepository
                .findFilialDepartmentByFilialIdAndDepartmentId(Long.valueOf(filialId), Long.valueOf(departmentId))
                .flatMap(
                        filialDepartment -> this.filialDepartmentRepository.deleteFilialDepartmentByFilialIdAndDepartmentId(
                                filialDepartment.getFilialId(), filialDepartment.getDepartmentId()
                        )
                                .thenReturn(FILIAL_DEPARTMENT_DELETED.getMessage())
                )
                .switchIfEmpty(
                        Mono.error(new FilialDepartmentNotFoundException(FILIAL_DEPARTMENT_NOT_FOUND.getMessage()))
                );
    }
}
