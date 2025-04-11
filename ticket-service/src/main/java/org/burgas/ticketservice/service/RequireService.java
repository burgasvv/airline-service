package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.RequireRequest;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.kafka.KafkaProducer;
import org.burgas.ticketservice.mapper.RequireMapper;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RequireService {

    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;
    private final KafkaProducer kafkaProducer;

    public RequireService(RequireRepository requireRepository, RequireMapper requireMapper, KafkaProducer kafkaProducer) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public Flux<RequireResponse> findAllByClosed(final String closed) {
        return this.requireRepository.findRequiresByClosed(Boolean.parseBoolean(closed))
                .flatMap(require -> this.requireMapper.toRequireResponse(Mono.fromCallable(() -> require)));
    }

    public Mono<RequireResponse> findById(final String requireId) {
        return this.requireRepository.findById(Long.valueOf(requireId))
                .flatMap(require -> this.requireMapper.toRequireResponse(Mono.fromCallable(() -> require)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<RequireResponse> createOrUpdate(final Mono<RequireRequest> requireRequestMono) {
        return requireRequestMono.flatMap(
                requireRequest -> this.requireMapper.toRequire(Mono.fromCallable(() -> requireRequest))
                        .flatMap(this.requireRepository::save)
                        .flatMap(require -> this.requireMapper.toRequireResponse(Mono.fromCallable(() -> require)))
                        .flatMap(
                                requireResponse -> this.kafkaProducer.sendStringRequireMessage(Mono.fromCallable(() -> requireResponse))
                                        .then(Mono.fromCallable(() -> requireResponse))
                        )
        );
    }
}
