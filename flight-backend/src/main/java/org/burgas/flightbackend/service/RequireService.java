package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.RequireRequest;
import org.burgas.flightbackend.dto.RequireResponse;
import org.burgas.flightbackend.exception.RequireNotCreatedException;
import org.burgas.flightbackend.exception.RequireNotFoundException;
import org.burgas.flightbackend.kafka.KafkaProducer;
import org.burgas.flightbackend.log.RequireLogs;
import org.burgas.flightbackend.mapper.RequireMapper;
import org.burgas.flightbackend.repository.RequireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.burgas.flightbackend.message.RequireMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RequireService {

    private static final Logger log = LoggerFactory.getLogger(RequireService.class);
    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;
    private final KafkaProducer kafkaProducer;

    public RequireService(RequireRepository requireRepository, RequireMapper requireMapper, KafkaProducer kafkaProducer) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public List<RequireResponse> findAllByClosed(final String closed) {
        return this.requireRepository.findRequiresByClosed(Boolean.parseBoolean(closed))
                .stream()
                .peek(require -> log.info(RequireLogs.REQUIRE_FOUND_ALL_BY_CLOSED.getLogMessage(), require))
                .map(this.requireMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<RequireResponse> findByUserId(final String userId) {
        return this.requireRepository.findRequiresByUserId(Long.valueOf(userId))
                .stream()
                .peek(require -> log.info(RequireLogs.REQUIRE_FOUND_BY_USER_ID.getLogMessage(), require))
                .map(this.requireMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<RequireResponse> findByAdminId(final String adminId) {
        return this.requireRepository.findRequiresByAdminId(Long.valueOf(adminId))
                .stream()
                .peek(require -> log.info(RequireLogs.REQUIRE_FOUND_BY_ADMIN_ID.getLogMessage(), require))
                .map(this.requireMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RequireResponse findById(final String requireId) {
        return this.requireRepository.findById(Long.valueOf(requireId))
                .stream()
                .peek(require -> log.info(RequireLogs.REQUIRE_FOUND_BY_ID.getLogMessage(), require))
                .map(this.requireMapper::toResponse)
                .findFirst()
                .orElseGet(RequireResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public RequireResponse createOrUpdate(final RequireRequest requireRequest) {
        return of(this.requireMapper.toEntity(requireRequest))
                .map(this.requireRepository::save)
                .map(this.requireMapper::toResponse)
                .map(
                        requireResponse -> {
                            this.kafkaProducer.sendStringRequireMessage(requireResponse);
                            return requireResponse;
                        }
                )
                .orElseThrow(
                        () -> new RequireNotCreatedException(REQUIRE_NOT_CREATED.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String requireId) {
        return this.requireRepository.findById(Long.parseLong(requireId))
                .map(
                        require -> {
                            this.requireRepository.deleteById(require.getId());
                            return REQUIRE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new RequireNotFoundException(REQUIRE_NOT_FOUND.getMessage()));
    }
}
