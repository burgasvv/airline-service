package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.RequireAnswerRequest;
import org.burgas.flightbackend.dto.RequireAnswerResponse;
import org.burgas.flightbackend.entity.RequireAnswerToken;
import org.burgas.flightbackend.exception.RequireAlreadyClosedException;
import org.burgas.flightbackend.exception.RequireAnswerNotTransformedException;
import org.burgas.flightbackend.kafka.KafkaProducer;
import org.burgas.flightbackend.mapper.RequireAnswerMapper;
import org.burgas.flightbackend.mapper.RequireAnswerTokenMapper;
import org.burgas.flightbackend.repository.RequireAnswerRepository;
import org.burgas.flightbackend.repository.RequireAnswerTokenRepository;
import org.burgas.flightbackend.repository.RequireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.burgas.flightbackend.log.RequireAnswerLogs.REQUIRE_ANSWER_FOUND_ALL_BY_ADMIN_ID;
import static org.burgas.flightbackend.log.RequireAnswerLogs.REQUIRE_ANSWER_FOUND_ALL_BY_USER_ID;
import static org.burgas.flightbackend.message.RequireAnswerMessages.REQUIRE_ANSWER_NOT_TRANSFORMED;
import static org.burgas.flightbackend.message.RequireMessages.REQUIRE_CLOSED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RequireAnswerService {

    private static final Logger log = LoggerFactory.getLogger(RequireAnswerService.class);

    private final RequireRepository requireRepository;
    private final RequireAnswerRepository requireAnswerRepository;
    private final RequireAnswerMapper requireAnswerMapper;
    private final RequireAnswerTokenRepository requireAnswerTokenRepository;
    private final RequireAnswerTokenMapper requireAnswerTokenMapper;
    private final KafkaProducer kafkaProducer;

    public RequireAnswerService(
            RequireRepository requireRepository, RequireAnswerRepository requireAnswerRepository, RequireAnswerMapper requireAnswerMapper,
            RequireAnswerTokenRepository requireAnswerTokenRepository, RequireAnswerTokenMapper requireAnswerTokenMapper, KafkaProducer kafkaProducer
    ) {
        this.requireRepository = requireRepository;
        this.requireAnswerRepository = requireAnswerRepository;
        this.requireAnswerMapper = requireAnswerMapper;
        this.requireAnswerTokenRepository = requireAnswerTokenRepository;
        this.requireAnswerTokenMapper = requireAnswerTokenMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public List<RequireAnswerResponse> findByUserId(final String userId) {
        return this.requireAnswerRepository.findRequireAnswersByUserId(Long.valueOf(userId))
                .stream()
                .peek(requireAnswer -> log.info(REQUIRE_ANSWER_FOUND_ALL_BY_USER_ID.getLogMessage(), requireAnswer))
                .map(this.requireAnswerMapper::toRequireAnswerResponse)
                .collect(Collectors.toList());
    }

    public List<RequireAnswerResponse> findByAdminId(final String adminId) {
        return this.requireAnswerRepository.findRequireAnswersByAdminId(Long.valueOf(adminId))
                .stream()
                .peek(requireAnswer -> log.info(REQUIRE_ANSWER_FOUND_ALL_BY_ADMIN_ID.getLogMessage(), requireAnswer))
                .map(this.requireAnswerMapper::toRequireAnswerResponse)
                .collect(Collectors.toList());
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Object sendAnswerOrToken(final RequireAnswerRequest requireAnswerRequest) {
        return this.requireRepository.findById(requireAnswerRequest.getRequireId())
                .map(
                        require -> {
                            if (!require.getClosed()) {
                                require.setClosed(true);
                                return this.requireRepository.save(require);

                            } else {
                                throw new RequireAlreadyClosedException(REQUIRE_CLOSED.getMessage());
                            }
                        }
                )
                .map(
                        _ -> {
                            if (requireAnswerRequest.getAllowed()) {
                                return of(this.requireAnswerMapper.toRequireAnswer(requireAnswerRequest))
                                        .map(this.requireAnswerRepository::save)
                                        .map(
                                                requireAnswer -> this.requireAnswerTokenRepository.save(
                                                        RequireAnswerToken.builder()
                                                                .value(UUID.randomUUID())
                                                                .requireAnswerId(requireAnswer.getId())
                                                                .build()
                                                )
                                        )
                                        .map(this.requireAnswerTokenMapper::toRequireAnswerTokenResponse)
                                        .map(
                                                requireAnswerTokenResponse -> {
                                                    this.kafkaProducer.sendStringRequireAnswerTokenMessage(requireAnswerTokenResponse);
                                                    return requireAnswerTokenResponse;
                                                }
                                        )
                                        .orElseThrow(
                                                () -> new RequireAnswerNotTransformedException(REQUIRE_ANSWER_NOT_TRANSFORMED.getLogMessages())
                                        );
                            } else {
                                return of(this.requireAnswerMapper.toRequireAnswer(requireAnswerRequest))
                                        .map(this.requireAnswerRepository::save)
                                        .map(this.requireAnswerMapper::toRequireAnswerResponse)
                                        .map(
                                                requireAnswerResponse -> {
                                                    this.kafkaProducer.sendStringRequireAnswerMessage(requireAnswerResponse);
                                                    return requireAnswerResponse;
                                                }
                                        )
                                        .orElseThrow(
                                                () -> new RequireAnswerNotTransformedException(REQUIRE_ANSWER_NOT_TRANSFORMED.getLogMessages())
                                        );
                            }
                        }
                );
    }
}
