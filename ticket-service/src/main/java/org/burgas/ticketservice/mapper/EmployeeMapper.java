package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.EmployeeRequest;
import org.burgas.ticketservice.dto.EmployeeResponse;
import org.burgas.ticketservice.entity.Employee;
import org.burgas.ticketservice.repository.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;

@Component
public final class EmployeeMapper {

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

    private <T> T getData(final T first, final T second) {
        return first == null || first == "" ? second : first;
    }

    public Mono<Employee> toEmployee(final Mono<EmployeeRequest> employeeRequestMono) {
        return employeeRequestMono.flatMap(
                employeeRequest -> {
                    Long employeeId = this.getData(employeeRequest.getId(), 0L);
                    return this.employeeRepository.findById(employeeId)
                            .flatMap(
                                    employee -> Mono.fromCallable(() ->
                                            Employee.builder()
                                                    .id(employee.getId())
                                                    .name(this.getData(employeeRequest.getName(), employee.getName()))
                                                    .surname(this.getData(employeeRequest.getSurname(), employee.getSurname()))
                                                    .patronymic(this.getData(employeeRequest.getPatronymic(), employee.getPatronymic()))
                                                    .about(this.getData(employeeRequest.getAbout(), employee.getAbout()))
                                                    .passport(this.getData(employeeRequest.getPassport(), employee.getPassport()))
                                                    .identityId(this.getData(employeeRequest.getIdentityId(), employee.getIdentityId()))
                                                    .addressId(this.getData(employeeRequest.getAddress().getId(), employee.getAddressId()))
                                                    .positionId(this.getData(employeeRequest.getPositionId(), employee.getPositionId()))
                                                    .filialDepartmentId(this.getData(employeeRequest.getFilialDepartmentId(),
                                                            employee.getFilialDepartmentId()))
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            Employee.builder()
                                                    .name(employeeRequest.getName())
                                                    .surname(employeeRequest.getSurname())
                                                    .patronymic(employeeRequest.getPatronymic())
                                                    .about(employeeRequest.getAbout())
                                                    .passport(employeeRequest.getPassport())
                                                    .identityId(employeeRequest.getIdentityId())
                                                    .addressId(employeeRequest.getAddress().getId())
                                                    .positionId(employeeRequest.getPositionId())
                                                    .filialDepartmentId(employeeRequest.getFilialDepartmentId())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<EmployeeResponse> toEmployeeResponse(final Mono<Employee> employeeMono) {
        return employeeMono.flatMap(
                employee -> Mono.zip(
                        this.identityRepository.findById(requireNonNull(employee.getId()))
                                .flatMap(identity -> this.identityMapper.toIdentityResponse(Mono.fromCallable(() -> identity))),
                        this.addressRepository.findById(employee.getAddressId())
                                .flatMap(address -> this.addressMapper.toAddressResponse(Mono.fromCallable(() -> address))),
                        this.positionRepository.findById(employee.getPositionId())
                                .flatMap(position -> this.positionMapper.toPositionResponse(Mono.fromCallable(() -> position))),
                        this.filialDepartmentRepository.findById(employee.getFilialDepartmentId())
                                .flatMap(filialDepartment -> this.filialDepartmentMapper.toFilialDepartmentResponse(Mono.fromCallable(() -> filialDepartment)))
                )
                        .flatMap(
                                objects ->
                                        Mono.fromCallable(() ->
                                                EmployeeResponse.builder()
                                                        .id(employee.getId())
                                                        .name(employee.getName())
                                                        .surname(employee.getSurname())
                                                        .patronymic(employee.getPatronymic())
                                                        .about(employee.getAbout())
                                                        .passport(employee.getPassport())
                                                        .identity(objects.getT1())
                                                        .address(objects.getT2())
                                                        .position(objects.getT3())
                                                        .filialDepartment(objects.getT4())
                                                        .build())
                        )
        );
    }
}
