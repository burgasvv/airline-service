package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.dto.EmployeeRequest;
import org.burgas.ticketservice.dto.EmployeeResponse;
import org.burgas.ticketservice.entity.Require;
import org.burgas.ticketservice.entity.RequireAnswer;
import org.burgas.ticketservice.exception.EmployeeNotCreatedException;
import org.burgas.ticketservice.exception.IdentityNotFoundException;
import org.burgas.ticketservice.exception.IdentityWrongTokenException;
import org.burgas.ticketservice.exception.WrongIdentityException;
import org.burgas.ticketservice.mapper.EmployeeMapper;
import org.burgas.ticketservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static org.burgas.ticketservice.log.EmployeeLogs.EMPLOYEE_FOUND_BY_ID;
import static org.burgas.ticketservice.message.EmployeeMessages.EMPLOYEE_NOT_CREATED;
import static org.burgas.ticketservice.message.IdentityMessages.*;
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
                .map(this.employeeMapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(final String employeeId) {
        return this.employeeRepository.findById(Long.valueOf(employeeId))
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BY_ID.getLogMessage(), employee))
                .map(this.employeeMapper::toEmployeeResponse)
                .findFirst()
                .orElseGet(EmployeeResponse::new);
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
                                                _ -> {
                                                    this.requireAnswerTokenRepository.deleteById(requireAnswerToken.getId());
                                                    return of(this.employeeMapper.toEmployee(employeeRequest))
                                                            .map(this.employeeRepository::save)
                                                            .map(this.employeeMapper::toEmployeeResponse)
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
}
