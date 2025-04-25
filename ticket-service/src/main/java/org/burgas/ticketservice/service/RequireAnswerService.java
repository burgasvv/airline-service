package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.RequireAnswerRequest;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.burgas.ticketservice.exception.RequireAlreadyClosedException;
import org.burgas.ticketservice.kafka.KafkaProducer;
import org.burgas.ticketservice.mapper.RequireAnswerMapper;
import org.burgas.ticketservice.mapper.RequireAnswerTokenMapper;
import org.burgas.ticketservice.repository.RequireAnswerRepository;
import org.burgas.ticketservice.repository.RequireAnswerTokenRepository;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.burgas.ticketservice.message.RequireMessage.REQUIRE_CLOSED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RequireAnswerService {

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
                .map(this.requireAnswerMapper::toRequireAnswerResponse)
                .toList();
    }

    public List<RequireAnswerResponse> findByAdminId(final String adminId) {
        return this.requireAnswerRepository.findRequireAnswersByAdminId(Long.valueOf(adminId))
                .stream()
                .map(this.requireAnswerMapper::toRequireAnswerResponse)
                .toList();
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
                                return Optional.of(this.requireAnswerMapper.toRequireAnswer(requireAnswerRequest))
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
                                                    this.kafkaProducer
                                                            .sendStringRequireAnswerTokenMessage(requireAnswerTokenResponse);
                                                    return requireAnswerTokenResponse;
                                                }
                                        )
                                        .orElseThrow();
                            } else {
                                return Optional.of(this.requireAnswerMapper.toRequireAnswer(requireAnswerRequest))
                                        .map(this.requireAnswerRepository::save)
                                        .map(this.requireAnswerMapper::toRequireAnswerResponse)
                                        .map(
                                                requireAnswerResponse -> {
                                                    this.kafkaProducer.sendStringRequireAnswerMessage(requireAnswerResponse);
                                                    return requireAnswerResponse;
                                                }
                                        )
                                        .orElseThrow();
                            }
                        }
                );
    }
}
