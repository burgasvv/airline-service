package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.PositionRequest;
import org.burgas.ticketservice.dto.PositionResponse;
import org.burgas.ticketservice.exception.PositionNotFoundException;
import org.burgas.ticketservice.mapper.PositionMapper;
import org.burgas.ticketservice.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<PositionResponse> findAll() {
        return this.positionRepository.findAll()
                .stream()
                .map(this.positionMapper::toPositionResponse)
                .toList();
    }

    public PositionResponse findById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .map(this.positionMapper::toPositionResponse)
                .orElseGet(PositionResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PositionResponse createOrUpdate(final PositionRequest positionRequest) {
        return Optional.of(this.positionMapper.toPosition(positionRequest))
                .map(this.positionRepository::save)
                .map(this.positionMapper::toPositionResponse)
                .orElseGet(PositionResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .map(
                        position -> {
                            this.positionRepository.deleteById(requireNonNull(position.getId()));
                            return POSITION_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new PositionNotFoundException(POSITION_NOT_FOUND.getMessage())
                );
    }

}
