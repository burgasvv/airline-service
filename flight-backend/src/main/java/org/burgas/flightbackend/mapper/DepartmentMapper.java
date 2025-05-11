package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.DepartmentRequest;
import org.burgas.flightbackend.dto.DepartmentResponse;
import org.burgas.flightbackend.entity.Department;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.DepartmentRepository;
import org.springframework.stereotype.Component;

@Component
public final class DepartmentMapper implements MapperDataHandler<DepartmentRequest, Department, DepartmentResponse> {

    private final DepartmentRepository departmentRepository;

    public DepartmentMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department toEntity(DepartmentRequest departmentRequest) {
        Long departmentId = this.getData(departmentRequest.getId(), 0L);
        return this.departmentRepository.findById(departmentId)
                .map(
                        department -> Department.builder()
                                .id(department.getId())
                                .name(this.getData(departmentRequest.getName(), department.getName()))
                                .description(this.getData(departmentRequest.getDescription(), department.getDescription()))
                                .build()
                )
                .orElseGet(
                        () -> Department.builder()
                                .name(departmentRequest.getName())
                                .description(departmentRequest.getDescription())
                                .build()
                );
    }

    @Override
    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
