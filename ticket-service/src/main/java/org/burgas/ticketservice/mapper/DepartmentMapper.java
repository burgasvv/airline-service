package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.entity.Department;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class DepartmentMapper implements MapperDataHandler {

    private final DepartmentRepository departmentRepository;

    public DepartmentMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Mono<Department> toDepartment(final Mono<DepartmentRequest> departmentRequestMono) {
        return departmentRequestMono.flatMap(
                departmentRequest -> {
                    Long departmentId = this.getData(departmentRequest.getId(), 0L);
                    return this.departmentRepository.findById(departmentId)
                            .flatMap(
                                    department -> Mono.fromCallable(() ->
                                            Department.builder()
                                                    .id(department.getId())
                                                    .name(this.getData(departmentRequest.getName(), department.getName()))
                                                    .description(this.getData(departmentRequest.getDescription(), department.getDescription()))
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            Department.builder()
                                                    .name(departmentRequest.getName())
                                                    .description(departmentRequest.getDescription())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<DepartmentResponse> toDepartmentResponse(final Mono<Department> departmentMono) {
        return departmentMono.flatMap(
                department -> Mono.fromCallable(() ->
                        DepartmentResponse.builder()
                                .id(department.getId())
                                .name(department.getName())
                                .description(department.getDescription())
                                .build())
        );
    }
}
