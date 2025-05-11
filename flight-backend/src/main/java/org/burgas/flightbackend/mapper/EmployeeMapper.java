package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.*;
import org.burgas.flightbackend.entity.Employee;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.*;
import org.springframework.stereotype.Component;

@Component
public final class EmployeeMapper implements MapperDataHandler<EmployeeRequest, Employee, EmployeeResponse> {

    private final EmployeeRepository employeeRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final FilialDepartmentRepository filialDepartmentRepository;
    private final FilialDepartmentMapper filialDepartmentMapper;

    public EmployeeMapper(
            EmployeeRepository employeeRepository, IdentityRepository identityRepository, IdentityMapper identityMapper,
            AddressRepository addressRepository, AddressMapper addressMapper, PositionRepository positionRepository,
            PositionMapper positionMapper, FilialDepartmentRepository filialDepartmentRepository, FilialDepartmentMapper filialDepartmentMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.filialDepartmentRepository = filialDepartmentRepository;
        this.filialDepartmentMapper = filialDepartmentMapper;
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
                                .addressId(this.getData(employeeRequest.getAddress().getId(), employee.getAddressId()))
                                .positionId(this.getData(employeeRequest.getPositionId(), employee.getPositionId()))
                                .filialDepartmentId(this.getData(employeeRequest.getFilialDepartmentId(), employee.getFilialDepartmentId()))
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
                                .addressId(employeeRequest.getAddress().getId())
                                .positionId(employeeRequest.getPositionId())
                                .filialDepartmentId(employeeRequest.getFilialDepartmentId())
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
                        this.identityRepository.findById(employee.getIdentityId())
                                .map(this.identityMapper::toResponse)
                                .orElseGet(IdentityResponse::new)
                )
                .address(
                        this.addressRepository.findById(employee.getAddressId())
                                .map(this.addressMapper::toResponse)
                                .orElseGet(AddressResponse::new)
                )
                .position(
                        this.positionRepository.findById(employee.getPositionId())
                                .map(this.positionMapper::toResponse)
                                .orElseGet(PositionResponse::new)
                )
                .filialDepartment(
                        this.filialDepartmentRepository.findById(employee.getFilialDepartmentId())
                                .map(this.filialDepartmentMapper::toResponse)
                                .orElseGet(FilialDepartmentResponse::new)
                )
                .build();
    }
}
