package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.PositionRequest;
import org.burgas.ticketservice.dto.PositionResponse;
import org.burgas.ticketservice.exception.PositionNotFoundException;
import org.burgas.ticketservice.mapper.PositionMapper;
import org.burgas.ticketservice.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static org.burgas.ticketservice.message.PositionMessage.POSITION_DELETED;
import static org.burgas.ticketservice.message.PositionMessage.POSITION_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    public Flux<PositionResponse> findAll() {
        return this.positionRepository.findAll()
                .flatMap(position -> this.positionMapper.toPositionResponse(Mono.fromCallable(() -> position)));
    }

    public Mono<PositionResponse> findById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .flatMap(position -> this.positionMapper.toPositionResponse(Mono.fromCallable(() -> position)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<PositionResponse> createOrUpdate(final Mono<PositionRequest> positionRequestMono) {
        return positionRequestMono.flatMap(
                positionRequest -> this.positionMapper.toPosition(Mono.fromCallable(() -> positionRequest))
                        .flatMap(this.positionRepository::save)
                        .flatMap(position -> this.positionMapper.toPositionResponse(Mono.fromCallable(() -> position)))
        );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .flatMap(
                        position -> this.positionRepository.deleteById(requireNonNull(position.getId()))
                                .thenReturn(POSITION_DELETED.getMessage())
                )
                .switchIfEmpty(
                        Mono.error(new PositionNotFoundException(POSITION_NOT_FOUND.getMessage()))
                );
    }
}
