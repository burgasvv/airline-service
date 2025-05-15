package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.EmployeeRequest;
import org.burgas.hotelbackend.dto.EmployeeResponse;
import org.burgas.hotelbackend.entity.Employee;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.*;
import org.springframework.stereotype.Component;


@Component
public final class EmployeeMapper implements MapperDataHandler<EmployeeRequest, Employee, EmployeeResponse> {

    private final EmployeeRepository employeeRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public EmployeeMapper(
            EmployeeRepository employeeRepository, IdentityRepository identityRepository, IdentityMapper identityMapper,
            FilialRepository filialRepository, FilialMapper filialMapper, PositionRepository positionRepository, PositionMapper positionMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public Employee toEntity(EmployeeRequest employeeRequest) {
        Long employeeId = this.getData(employeeRequest.getId(), 0L);
        return this.employeeRepository.findById(employeeId)
                .map(
                        employee -> Employee.builder()
                                .id(employee.getId())
                                .name(this.getData(employeeRequest.getName(), employee.getName()))
                                .surname(this.getData(employeeRequest.getSurname(), employee.getSurname()))
                                .patronymic(this.getData(employeeRequest.getPatronymic(), employee.getPatronymic()))
                                .about(this.getData(employeeRequest.getAbout(), employee.getAbout()))
                                .passport(this.getData(employeeRequest.getPassport(), employee.getPassport()))
                                .identityId(this.getData(employeeRequest.getIdentityId(), employee.getIdentityId()))
                                .filialId(this.getData(employeeRequest.getFilialId(), employee.getFilialId()))
                                .positionId(this.getData(employeeRequest.getPositionId(), employee.getPositionId()))
                                .imageId(this.getData(employeeRequest.getImageId(), employee.getImageId()))
                                .build()
                )
                .orElseGet(
                        () -> Employee.builder()
                                .name(employeeRequest.getName())
                                .surname(employeeRequest.getSurname())
                                .patronymic(employeeRequest.getPatronymic())
                                .about(employeeRequest.getAbout())
                                .passport(employeeRequest.getPassport())
                                .identityId(employeeRequest.getIdentityId())
                                .filialId(employeeRequest.getFilialId())
                                .positionId(employeeRequest.getPositionId())
                                .imageId(employeeRequest.getImageId())
                                .build()
                );
    }

    @Override
    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .patronymic(employee.getPatronymic())
                .about(employee.getAbout())
                .passport(employee.getPassport())
                .identity(
                        this.identityRepository.findById(employee.getIdentityId() == null ? 0L : employee.getIdentityId())
                                .map(this.identityMapper::toResponse)
                                .orElse(null)
                )
                .filial(
                        this.filialRepository.findById(employee.getFilialId() == null ? 0L : employee.getFilialId())
                                .map(this.filialMapper::toResponse)
                                .orElse(null)
                )
                .position(
                        this.positionRepository.findById(employee.getPositionId() == null ? 0L : employee.getPositionId())
                                .map(this.positionMapper::toResponse)
                                .orElse(null)
                )
                .imageId(employee.getImageId())
                .build();
    }
}
