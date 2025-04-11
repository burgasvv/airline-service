package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.RequireAnswerRequest;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.burgas.ticketservice.exception.RequireAlreadyClosedException;
import org.burgas.ticketservice.exception.RequireNotFoundException;
import org.burgas.ticketservice.kafka.KafkaProducer;
import org.burgas.ticketservice.mapper.RequireAnswerMapper;
import org.burgas.ticketservice.mapper.RequireAnswerTokenMapper;
import org.burgas.ticketservice.repository.RequireAnswerRepository;
import org.burgas.ticketservice.repository.RequireAnswerTokenRepository;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.burgas.ticketservice.message.RequireMessage.REQUIRE_CLOSED;
import static org.burgas.ticketservice.message.RequireMessage.REQUIRE_NOT_FOUND;
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

    public Flux<RequireAnswerResponse> findByUserId(final String userId) {
        return this.requireAnswerRepository.findRequireAnswersByUserId(Long.valueOf(userId))
                .flatMap(requireAnswer -> this.requireAnswerMapper.toRequireAnswerResponse(Mono.fromCallable(() -> requireAnswer)));
    }

    public Flux<RequireAnswerResponse> findByAdminId(final String adminId) {
        return this.requireAnswerRepository.findRequireAnswersByAdminId(Long.valueOf(adminId))
                .flatMap(requireAnswer -> this.requireAnswerMapper.toRequireAnswerResponse(Mono.fromCallable(() -> requireAnswer)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<Object> sendAnswerOrToken(final Mono<RequireAnswerRequest> requireAnswerRequestMono) {
        return requireAnswerRequestMono.flatMap(
                requireAnswerRequest -> this.requireRepository.findById(requireAnswerRequest.getRequireId())
                        .flatMap(
                                require -> {
                                    if (!require.getClosed()) {
                                        require.setClosed(true);
                                        require.setNew(false);
                                        return this.requireRepository.save(require);

                                    } else {
                                        return Mono.error(new RequireAlreadyClosedException(REQUIRE_CLOSED.getMessage()));
                                    }
                                }
                        )
                        .switchIfEmpty(
                                Mono.error(new RequireNotFoundException(REQUIRE_NOT_FOUND.getMessage()))
                        )
                        .flatMap(
                                _ -> {
                                    if (requireAnswerRequest.getAllowed()) {
                                        return this.requireAnswerMapper.toRequireAnswer(Mono.fromCallable(() -> requireAnswerRequest))
                                                .flatMap(this.requireAnswerRepository::save)
                                                .flatMap(
                                                        requireAnswer -> this.requireAnswerTokenRepository.save(
                                                                RequireAnswerToken.builder()
                                                                        .value(UUID.randomUUID())
                                                                        .requireAnswerId(requireAnswer.getId())
                                                                        .isNew(true)
                                                                        .build()
                                                        )
                                                )
                                                .flatMap(requireAnswerToken -> this.requireAnswerTokenMapper
                                                        .toRequireAnswerTokenResponse(Mono.fromCallable(() -> requireAnswerToken)))
                                                .flatMap(
                                                        requireAnswerTokenResponse -> this.kafkaProducer
                                                                .sendStringRequireAnswerTokenMessage(Mono.fromCallable(() -> requireAnswerTokenResponse))
                                                                .then(Mono.fromCallable(() -> requireAnswerTokenResponse))
                                                );
                                    } else {
                                        return this.requireAnswerMapper.toRequireAnswer(Mono.fromCallable(() -> requireAnswerRequest))
                                                .flatMap(this.requireAnswerRepository::save)
                                                .flatMap(requireAnswer -> this.requireAnswerMapper
                                                        .toRequireAnswerResponse(Mono.fromCallable(() -> requireAnswer)))
                                                .flatMap(
                                                        requireAnswerResponse -> this.kafkaProducer
                                                                .sendStringRequireAnswerMessage(Mono.fromCallable(() -> requireAnswerResponse))
                                                                .then(Mono.fromCallable(() -> requireAnswerResponse))
                                                );
                                    }
                                }
                        )
        );
    }
}
