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
import reactor.core.publisher.Mono;

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

    public Mono<FilialDepartment> toFilialDepartment(final Mono<FilialDepartmentRequest> filialDepartmentRequestMono) {
        return filialDepartmentRequestMono.flatMap(
                filialDepartmentRequest -> {
                    Long filialDepartmentId = this.getData(filialDepartmentRequest.getId(), 0L);
                    return this.filialDepartmentRepository.findById(filialDepartmentId)
                            .flatMap(
                                    filialDepartment -> Mono.fromCallable(() ->
                                            FilialDepartment.builder()
                                                    .id(filialDepartment.getId())
                                                    .filialId(this.getData(filialDepartmentRequest.getFilial().getId(),
                                                            filialDepartment.getFilialId())
                                                    )
                                                    .departmentId(this.getData(filialDepartmentRequest.getDepartment().getId(),
                                                            filialDepartment.getDepartmentId())
                                                    )
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            FilialDepartment.builder()
                                                    .filialId(filialDepartmentRequest.getFilial().getId())
                                                    .departmentId(filialDepartmentRequest.getDepartment().getId())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<FilialDepartmentResponse> toFilialDepartmentResponse(final Mono<FilialDepartment> filialDepartmentMono) {
        return filialDepartmentMono.flatMap(
                filialDepartment -> Mono.zip(
                        this.filialRepository.findById(filialDepartment.getFilialId())
                                .flatMap(filial -> this.filialMapper.toFilialResponse(Mono.fromCallable(() -> filial))),
                        this.departmentRepository.findById(filialDepartment.getDepartmentId())
                                .flatMap(department -> this.departmentMapper.toDepartmentResponse(Mono.fromCallable(() -> department)))
                )
                        .flatMap(
                                objects -> {
                                    FilialResponse filialResponse = objects.getT1();
                                    DepartmentResponse departmentResponse = objects.getT2();
                                    return Mono.fromCallable(() ->
                                            FilialDepartmentResponse.builder()
                                                    .id(filialDepartment.getId())
                                                    .filial(filialResponse)
                                                    .department(departmentResponse)
                                                    .build());
                                }
                        )
        );
    }
}
