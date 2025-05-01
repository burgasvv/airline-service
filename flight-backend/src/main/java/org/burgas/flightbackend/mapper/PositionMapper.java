package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.DepartmentResponse;
import org.burgas.flightbackend.dto.PositionRequest;
import org.burgas.flightbackend.dto.PositionResponse;
import org.burgas.flightbackend.entity.Position;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.DepartmentRepository;
import org.burgas.flightbackend.repository.PositionRepository;
import org.springframework.stereotype.Component;

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

    public Position toPosition(final PositionRequest positionRequest) {
        Long positionId = this.getData(positionRequest.getId(), 0L);
        return this.positionRepository.findById(positionId)
                .map(
                        position -> Position.builder()
                                .id(position.getId())
                                .name(this.getData(positionRequest.getName(), position.getName()))
                                .description(this.getData(positionRequest.getDescription(), position.getDescription()))
                                .departmentId(this.getData(positionRequest.getDepartmentId(), position.getDepartmentId()))
                                .build()
                )
                .orElseGet(
                        () -> Position.builder()
                                .name(positionRequest.getName())
                                .description(positionRequest.getDescription())
                                .departmentId(positionRequest.getDepartmentId())
                                .build()
                );
    }

    public PositionResponse toPositionResponse(final Position position) {
        return PositionResponse.builder()
                .id(position.getId())
                .name(position.getName())
                .description(position.getDescription())
                .department(
                        this.departmentRepository.findById(position.getDepartmentId())
                                .map(this.departmentMapper::toDepartmentResponse)
                                .orElseGet(DepartmentResponse::new)
                )
                .build();
    }
}
