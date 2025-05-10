package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.DepartmentRequest;
import org.burgas.hotelbackend.dto.DepartmentResponse;
import org.burgas.hotelbackend.entity.Department;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.DepartmentRepository;
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
