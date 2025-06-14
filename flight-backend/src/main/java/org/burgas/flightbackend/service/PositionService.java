package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.PositionRequest;
import org.burgas.flightbackend.dto.PositionResponse;
import org.burgas.flightbackend.exception.PositionNotCreatedException;
import org.burgas.flightbackend.exception.PositionNotFoundException;
import org.burgas.flightbackend.mapper.PositionMapper;
import org.burgas.flightbackend.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.flightbackend.log.PositionLogs.*;
import static org.burgas.flightbackend.message.PositionMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
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
                .map(this.positionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<PositionResponse>> findAllAsync() {
        return supplyAsync(this.positionRepository::findAll)
                .thenApplyAsync(
                        positions -> positions.stream()
                                .peek(position -> log.info(POSITION_FOUND_ALL_ASYNC.getLogMessage(), position))
                                .map(this.positionMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<PositionResponse> findAllPages(final Integer page, final Integer size) {
        return this.positionRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "name"))
                .map(this.positionMapper::toResponse);
    }

    public PositionResponse findById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId == null ? "0" : positionId))
                .stream()
                .peek(position -> log.info(POSITION_FOUND_BY_ID.getLogMessage(), position))
                .map(this.positionMapper::toResponse)
                .findFirst()
                .orElseGet(PositionResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<PositionResponse> findByIdAsync(final String positionId) {
        return supplyAsync(() -> this.positionRepository.findById(Long.parseLong(positionId == null ? "0" : positionId)))
                .thenApplyAsync(
                        position -> position.stream()
                                .peek(foundPosition -> log.info(POSITION_FOUND_BY_ID_ASYNC.getLogMessage(), foundPosition))
                                .map(this.positionMapper::toResponse)
                                .findFirst()
                                .orElseGet(PositionResponse::new)
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PositionResponse createOrUpdate(final PositionRequest positionRequest) {
        return of(this.positionMapper.toEntity(positionRequest))
                .map(this.positionRepository::save)
                .map(this.positionMapper::toResponse)
                .orElseThrow(
                        () -> new PositionNotCreatedException(POSITION_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<PositionResponse> createOrUpdateAsync(final PositionRequest positionRequest) {
        return supplyAsync(() -> this.positionMapper.toEntity(positionRequest))
                .thenApplyAsync(this.positionRepository::save)
                .thenApplyAsync(this.positionMapper::toResponse);
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String positionId) {
        return this.positionRepository.findById(Long.valueOf(positionId == null ? "0" : positionId))
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

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String positionId) {
        return supplyAsync(() -> this.positionRepository.findById(Long.parseLong(positionId == null ? "0" : positionId)))
                .thenApplyAsync(
                        position -> position.stream()
                                .peek(foundPosition -> log.info(POSITION_FOUND_ALL_ASYNC.getLogMessage(), foundPosition))
                                .map(
                                        foundPosition -> {
                                            this.positionRepository.deleteById(foundPosition.getId());
                                            return POSITION_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new PositionNotFoundException(POSITION_NOT_FOUND.getMessage())
                                )
                );
    }
}
