package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.AddressResponse;
import org.burgas.flightbackend.dto.EmployeeRequest;
import org.burgas.flightbackend.dto.EmployeeResponse;
import org.burgas.flightbackend.entity.Require;
import org.burgas.flightbackend.entity.RequireAnswer;
import org.burgas.flightbackend.exception.*;
import org.burgas.flightbackend.log.EmployeeLogs;
import org.burgas.flightbackend.mapper.EmployeeMapper;
import org.burgas.flightbackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.flightbackend.log.EmployeeLogs.EMPLOYEE_FOUND_BY_ID;
import static org.burgas.flightbackend.log.EmployeeLogs.EMPLOYEE_FOUND_BY_ID_ASYNC;
import static org.burgas.flightbackend.message.EmployeeMessages.*;
import static org.burgas.flightbackend.message.IdentityMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressService addressService;
    private final IdentityRepository identityRepository;
    private final RequireAnswerTokenRepository requireAnswerTokenRepository;
    private final RequireAnswerRepository requireAnswerRepository;
    private final RequireRepository requireRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
            AddressService addressService, IdentityRepository identityRepository,
            RequireAnswerTokenRepository requireAnswerTokenRepository,
            RequireAnswerRepository requireAnswerRepository, RequireRepository requireRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.addressService = addressService;
        this.identityRepository = identityRepository;
        this.requireAnswerTokenRepository = requireAnswerTokenRepository;
        this.requireAnswerRepository = requireAnswerRepository;
        this.requireRepository = requireRepository;
    }

    public List<EmployeeResponse> finaAll() {
        return this.employeeRepository.findAll()
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BY_ID.getLogMessage(), employee))
                .map(this.employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<EmployeeResponse>> findAllAsync() {
        return supplyAsync(this.employeeRepository::findAll)
                .thenApplyAsync(
                        employees -> employees.stream()
                                .peek(employee -> log.info(EmployeeLogs.EMPLOYEE_FOUND_ALL_ASYNC.getLogMessage(), employee))
                                .map(this.employeeMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<EmployeeResponse> findAllPages(final Integer page, final Integer size) {
        return this.employeeRepository.findAll(
                        PageRequest.of(page - 1, size, Sort.Direction.ASC, "name", "surname", "patronymic")
                )
                .map(this.employeeMapper::toResponse);
    }

    public EmployeeResponse findById(final String employeeId) {
        return this.employeeRepository.findById(Long.valueOf(employeeId == null ? "0" : employeeId))
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BY_ID.getLogMessage(), employee))
                .map(this.employeeMapper::toResponse)
                .findFirst()
                .orElseGet(EmployeeResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<EmployeeResponse> findByIdAsync(final String employeeId) {
        return supplyAsync(() -> this.employeeRepository.findById(Long.parseLong(employeeId == null ? "0" : employeeId)))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BY_ID_ASYNC.getLogMessage(), foundEmployee))
                                .map(this.employeeMapper::toResponse)
                                .findFirst()
                                .orElseGet(EmployeeResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public EmployeeResponse createEmployee(final EmployeeRequest employeeRequest, final String token) {
        return this.requireAnswerTokenRepository.findRequireAnswerTokenByValue(UUID.fromString(token))
                .map(
                        requireAnswerToken -> {
                            RequireAnswer requireAnswer = this.requireAnswerRepository
                                    .findById(requireAnswerToken.getRequireAnswerId()).orElse(null);
                            Require require = this.requireRepository
                                    .findById(requireNonNull(requireAnswer).getRequireId()).orElse(null);

                            if (employeeRequest.getIdentityId().equals(requireNonNull(require).getUserId())) {

                                AddressResponse addressResponse = this.addressService.createOrUpdateSecured(employeeRequest.getAddress());
                                employeeRequest.getAddress().setId(addressResponse.getId());
                                employeeRequest.setName(require.getName());
                                employeeRequest.setSurname(require.getSurname());
                                employeeRequest.setPatronymic(require.getPatronymic());
                                employeeRequest.setPassport(require.getPassport());

                                return this.identityRepository.findById(employeeRequest.getIdentityId())
                                        .map(
                                                identity -> {
                                                    identity.setAuthorityId(2L);
                                                    return this.identityRepository.save(identity);
                                                }
                                        )
                                        .map(
                                                identity -> {
                                                    this.requireAnswerTokenRepository.deleteById(requireAnswerToken.getId());
                                                    return of(this.employeeMapper.toEntity(employeeRequest))
                                                            .map(this.employeeRepository::save)
                                                            .map(this.employeeMapper::toResponse)
                                                            .orElseThrow(
                                                                    () -> new EmployeeNotCreatedException(EMPLOYEE_NOT_CREATED.getMessage())
                                                            );
                                                }
                                        )
                                        .orElseThrow(
                                                () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                                        );

                            } else {
                                throw new WrongIdentityException(IDENTITY_WRONG_ID.getMessage());
                            }
                        }
                )
                .orElseThrow(
                        () -> new IdentityWrongTokenException(IDENTITY_WRONG_TOKEN.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<EmployeeResponse> createEmployeeAsync(final EmployeeRequest employeeRequest, final String token) {
        return supplyAsync(() -> this.createEmployee(employeeRequest, token));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public EmployeeResponse updateEmployee(final EmployeeRequest employeeRequest) {
        return of(this.employeeMapper.toEntity(employeeRequest))
                .map(this.employeeRepository::save)
                .map(this.employeeMapper::toResponse)
                .orElseThrow(
                        () -> new EmployeeNotCreatedException(EMPLOYEE_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<EmployeeResponse> updateEmployeeAsync(final EmployeeRequest employeeRequest) {
        return supplyAsync(() -> this.employeeMapper.toEntity(employeeRequest))
                .thenApplyAsync(this.employeeRepository::save)
                .thenApplyAsync(this.employeeMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String employeeId) {
        return this.employeeRepository.findById(Long.parseLong(employeeId == null ? "0" : employeeId))
                .map(
                        employee -> {
                            this.employeeRepository.deleteById(employee.getId());
                            return EMPLOYEE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String employeeId) {
        return supplyAsync(() -> this.employeeRepository.findById(Long.parseLong(employeeId == null ? "0" : employeeId)))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EmployeeLogs.EMPLOYEE_FOUND_BEFORE_DELETE.getLogMessage(), foundEmployee))
                                .map(
                                        foundEmployee -> {
                                            this.employeeRepository.deleteById(foundEmployee.getId());
                                            return EMPLOYEE_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                                )
                );
    }
}
