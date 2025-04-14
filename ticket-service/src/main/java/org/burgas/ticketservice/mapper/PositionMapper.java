package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.PositionRequest;
import org.burgas.ticketservice.dto.PositionResponse;
import org.burgas.ticketservice.entity.Position;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.DepartmentRepository;
import org.burgas.ticketservice.repository.PositionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class PositionMapper implements MapperDataHandler {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public PositionMapper(PositionRepository positionRepository, DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public Mono<Position> toPosition(final Mono<PositionRequest> positionRequestMono) {
        return positionRequestMono.flatMap(
                positionRequest -> {
                    Long positionId = this.getData(positionRequest.getId(), 0L);
                    return this.positionRepository.findById(positionId)
                            .flatMap(
                                    position -> Mono.fromCallable(() ->
                                            Position.builder()
                                                    .id(position.getId())
                                                    .name(this.getData(positionRequest.getName(), position.getName()))
                                                    .description(this.getData(positionRequest.getDescription(), position.getDescription()))
                                                    .departmentId(this.getData(positionRequest.getDepartmentId(), position.getDepartmentId()))
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            Position.builder()
                                                    .name(positionRequest.getName())
                                                    .description(positionRequest.getDescription())
                                                    .departmentId(positionRequest.getDepartmentId())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<PositionResponse> toPositionResponse(final Mono<Position> positionMono) {
        return positionMono.flatMap(
                position -> this.departmentRepository.findById(position.getDepartmentId())
                        .flatMap(department -> this.departmentMapper.toDepartmentResponse(Mono.fromCallable(() -> department)))
                        .flatMap(
                                departmentResponse -> Mono.fromCallable(() ->
                                        PositionResponse.builder()
                                                .id(position.getId())
                                                .name(position.getName())
                                                .description(position.getDescription())
                                                .department(departmentResponse)
                                                .build())
                        )
        );
    }
}
