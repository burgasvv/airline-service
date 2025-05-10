package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.PositionRequest;
import org.burgas.hotelbackend.dto.PositionResponse;
import org.burgas.hotelbackend.entity.Position;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.DepartmentRepository;
import org.burgas.hotelbackend.repository.PositionRepository;
import org.springframework.stereotype.Component;

@Component
public final class PositionMapper implements MapperDataHandler<PositionRequest, Position, PositionResponse> {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public PositionMapper(PositionRepository positionRepository, DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Position toEntity(PositionRequest positionRequest) {
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

    @Override
    public PositionResponse toResponse(Position position) {
        return PositionResponse.builder()
                .id(position.getId())
                .name(position.getName())
                .description(position.getDescription())
                .department(
                        this.departmentRepository.findById(position.getDepartmentId())
                                .map(this.departmentMapper::toResponse)
                                .orElse(null)
                )
                .build();
    }
}
