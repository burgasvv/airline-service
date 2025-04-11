package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.EmployeeRequest;
import org.burgas.ticketservice.dto.EmployeeResponse;
import org.burgas.ticketservice.exception.IdentityWrongTokenException;
import org.burgas.ticketservice.exception.WrongIdentityException;
import org.burgas.ticketservice.mapper.EmployeeMapper;
import org.burgas.ticketservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.burgas.ticketservice.message.IdentityMessage.IDENTITY_WRONG_ID;
import static org.burgas.ticketservice.message.IdentityMessage.IDENTITY_WRONG_TOKEN;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class EmployeeService {

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

    public Flux<EmployeeResponse> finaAll() {
        return this.employeeRepository.findAll()
                .flatMap(employee -> this.employeeMapper.toEmployeeResponse(Mono.fromCallable(() -> employee)));
    }

    public Mono<EmployeeResponse> findById(final String employeeId) {
        return this.employeeRepository.findById(Long.valueOf(employeeId))
                .flatMap(employee -> this.employeeMapper.toEmployeeResponse(Mono.fromCallable(() -> employee)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<EmployeeResponse> createEmployee(final Mono<EmployeeRequest> employeeRequestMono, final String token) {
        return this.requireAnswerTokenRepository.findRequireAnswerTokenByValue(UUID.fromString(token))
                .flatMap(
                        requireAnswerToken -> this.requireAnswerRepository.findById(requireAnswerToken.getRequireAnswerId())
                                .flatMap(requireAnswer -> this.requireRepository.findById(requireAnswer.getRequireId()))
                                .flatMap(
                                        require -> employeeRequestMono.flatMap(
                                                employeeRequest -> {
                                                    if (employeeRequest.getIdentityId().equals(require.getUserId())) {
                                                        return this.addressService.createOrUpdateSecured(Mono.fromCallable(employeeRequest::getAddress))
                                                                .flatMap(
                                                                        addressResponse -> {
                                                                            employeeRequest.getAddress().setId(addressResponse.getId());
                                                                            employeeRequest.setName(require.getName());
                                                                            employeeRequest.setSurname(require.getSurname());
                                                                            employeeRequest.setPatronymic(require.getPatronymic());
                                                                            employeeRequest.setPassport(require.getPassport());
                                                                            return this.identityRepository.findById(employeeRequest.getIdentityId())
                                                                                    .flatMap(
                                                                                            identity -> {
                                                                                                identity.setAuthorityId(2L);
                                                                                                identity.setNew(false);
                                                                                                return this.identityRepository.save(identity);
                                                                                            }
                                                                                    )
                                                                                    .flatMap(
                                                                                            _ -> this.requireAnswerTokenRepository.deleteById(requireNonNull(requireAnswerToken.getId()))
                                                                                                    .then(
                                                                                                            this.employeeMapper.toEmployee(Mono.fromCallable(() -> employeeRequest))
                                                                                                                    .flatMap(this.employeeRepository::save)
                                                                                                                    .flatMap(employee -> this.employeeMapper.toEmployeeResponse(Mono.fromCallable(() -> employee)))
                                                                                                    )
                                                                                    );
                                                                        }
                                                                );

                                                    } else {
                                                        return Mono.error(new WrongIdentityException(IDENTITY_WRONG_ID.getMessage()));
                                                    }
                                                }
                                        )
                                )
                )
                .switchIfEmpty(
                        Mono.error(new IdentityWrongTokenException(IDENTITY_WRONG_TOKEN.getMessage()))
                );
    }
}
