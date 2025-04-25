package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.entity.Department;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.springframework.stereotype.Component;

@Component
public final class DepartmentMapper implements MapperDataHandler {

    private final DepartmentRepository departmentRepository;

    public DepartmentMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department toDepartment(final DepartmentRequest departmentRequest) {
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

    public DepartmentResponse toDepartmentResponse(final Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
