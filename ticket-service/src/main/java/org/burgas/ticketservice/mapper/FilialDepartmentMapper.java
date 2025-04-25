package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.dto.FilialDepartmentRequest;
import org.burgas.ticketservice.dto.FilialDepartmentResponse;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.entity.FilialDepartment;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.burgas.ticketservice.repository.FilialDepartmentRepository;
import org.burgas.ticketservice.repository.FilialRepository;
import org.springframework.stereotype.Component;

@Component
public final class FilialDepartmentMapper implements MapperDataHandler {

    private final FilialDepartmentRepository filialDepartmentRepository;
    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public FilialDepartmentMapper(
            FilialDepartmentRepository filialDepartmentRepository, FilialRepository filialRepository,
            FilialMapper filialMapper, DepartmentRepository departmentRepository, DepartmentMapper departmentMapper
    ) {
        this.filialDepartmentRepository = filialDepartmentRepository;
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public FilialDepartment toFilialDepartment(final FilialDepartmentRequest filialDepartmentRequest) {
        Long filialDepartmentId = this.getData(filialDepartmentRequest.getId(), 0L);
        return this.filialDepartmentRepository.findById(filialDepartmentId)
                .map(
                        filialDepartment -> FilialDepartment.builder()
                                .id(filialDepartment.getId())
                                .filialId(this.getData(filialDepartmentRequest.getFilial().getId(), filialDepartment.getFilialId()))
                                .departmentId(this.getData(filialDepartmentRequest.getDepartment().getId(), filialDepartment.getDepartmentId()))
                                .build()
                )
                .orElseGet(
                        () -> FilialDepartment.builder()
                                .filialId(filialDepartmentRequest.getFilial().getId())
                                .departmentId(filialDepartmentRequest.getDepartment().getId())
                                .build()
                );
    }

    public FilialDepartmentResponse toFilialDepartmentResponse(final FilialDepartment filialDepartment) {
        return FilialDepartmentResponse.builder()
                .id(filialDepartment.getId())
                .filial(
                        this.filialRepository.findById(filialDepartment.getFilialId())
                                .map(this.filialMapper::toFilialResponse)
                                .orElseGet(FilialResponse::new)
                )
                .department(
                        this.departmentRepository.findById(filialDepartment.getDepartmentId())
                                .map(this.departmentMapper::toDepartmentResponse)
                                .orElseGet(DepartmentResponse::new)
                )
                .build();
    }
}
