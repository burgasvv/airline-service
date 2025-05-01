package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.PositionRequest;
import org.burgas.flightbackend.dto.PositionResponse;
import org.burgas.flightbackend.exception.PositionNotCreatedException;
import org.burgas.flightbackend.exception.PositionNotFoundException;
import org.burgas.flightbackend.mapper.PositionMapper;
import org.burgas.flightbackend.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static org.burgas.flightbackend.log.PositionLogs.*;
import static org.burgas.flightbackend.message.PositionMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PositionService {

    private static final Logger log = LoggerFactory.getLogger(PositionService.class);
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    public List<PositionResponse> findAll() {
        return this.positionRepository.findAll()
                .stream()
                .peek(position -> log.info(POSITION_FOUND_ALL.getLogMessage(), position))
                .map(this.positionMapper::toPositionResponse)
                .collect(Collectors.toList());
    }

    public PositionResponse findById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .stream()
                .peek(position -> log.info(POSITION_FOUND_BY_ID.getLogMessage(), position))
                .map(this.positionMapper::toPositionResponse)
                .findFirst()
                .orElseGet(PositionResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PositionResponse createOrUpdate(final PositionRequest positionRequest) {
        return of(this.positionMapper.toPosition(positionRequest))
                .map(this.positionRepository::save)
                .map(this.positionMapper::toPositionResponse)
                .orElseThrow(
                        () -> new PositionNotCreatedException(POSITION_NOT_CREATED.getMessage())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId))
                .stream()
                .peek(position -> log.info(POSITION_FOUND_BEFORE_DELETE.getLogMessage(), position))
                .map(
                        position -> {
                            this.positionRepository.deleteById(requireNonNull(position.getId()));
                            log.info(POSITION_DELETED_LOG.getLogMessage(), position);
                            return POSITION_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new PositionNotFoundException(POSITION_NOT_FOUND.getMessage())
                );
    }

}
