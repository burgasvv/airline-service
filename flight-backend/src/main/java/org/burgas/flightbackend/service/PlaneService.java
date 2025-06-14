package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.PlaneRequest;
import org.burgas.flightbackend.dto.PlaneResponse;
import org.burgas.flightbackend.exception.PlaneNotCreateException;
import org.burgas.flightbackend.exception.PlaneNotFoundException;
import org.burgas.flightbackend.log.PlaneLogs;
import org.burgas.flightbackend.mapper.PlaneMapper;
import org.burgas.flightbackend.repository.PlaneRepository;
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

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.*;
import static org.burgas.flightbackend.log.PlaneLogs.*;
import static org.burgas.flightbackend.message.PlaneMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PlaneService {

    private static final Logger log = LoggerFactory.getLogger(PlaneService.class);
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;

    public PlaneService(PlaneRepository planeRepository, PlaneMapper planeMapper) {
        this.planeRepository = planeRepository;
        this.planeMapper = planeMapper;
    }

    public List<PlaneResponse> findAll() {
        return this.planeRepository.findAll()
                .stream()
                .peek(plane -> log.info(PLANE_FOUND_ALL.getLogMessage(), plane))
                .map(this.planeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<PlaneResponse>> findAllAsync() {
        return supplyAsync(this.planeRepository::findAll)
                .thenApplyAsync(
                        planes -> planes.stream()
                                .peek(plane -> log.info(PLANE_FOUND_ALL_ASYNC.getLogMessage(), plane))
                                .map(this.planeMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<PlaneResponse> findAllPages(final Integer page, final Integer size) {
        return this.planeRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "model"))
                .map(this.planeMapper::toResponse);
    }

    public List<PlaneResponse> findAllByFree(final String free) {
        return this.planeRepository.findPlanesByFree(Boolean.parseBoolean(free))
                .stream()
                .peek(plane -> log.info(PlaneLogs.PLANE_FOUND_BY_FREE.getLogMessage(), plane))
                .map(this.planeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<PlaneResponse>> findAllByFreeAsync(final String free) {
        return supplyAsync(() -> this.planeRepository.findPlanesByFree(Boolean.parseBoolean(free)))
                .thenApplyAsync(
                        planes -> planes.stream()
                                .peek(plane -> log.info(PLANE_FOUND_BY_FREE_ASYNC.getLogMessage(), plane))
                                .map(this.planeMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public PlaneResponse findById(final String planeId) {
        return this.planeRepository.findById(Long.parseLong(planeId == null ? "0" : planeId))
                .stream()
                .peek(plane -> log.info(PlaneLogs.PLANE_FOUND_BY_ID.getLogMessage(), plane))
                .map(this.planeMapper::toResponse)
                .findFirst()
                .orElseGet(PlaneResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<PlaneResponse> findByIdAsync(final String planeId) {
        return supplyAsync(() -> this.planeRepository.findById(Long.parseLong(planeId == null ? "0" : planeId)))
                .thenApplyAsync(
                        plane -> plane.stream()
                                .peek(foundPlane -> log.info(PLANE_FOUND_BY_ID_ASYNC.getLogMessage(), foundPlane))
                                .map(this.planeMapper::toResponse)
                                .findFirst()
                                .orElseGet(PlaneResponse::new)
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PlaneResponse createOrUpdate(final PlaneRequest planeRequest) {
        return of(this.planeMapper.toEntity(planeRequest))
                .map(this.planeRepository::save)
                .map(this.planeMapper::toResponse)
                .orElseThrow(
                        () -> new PlaneNotCreateException(PLANE_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<PlaneResponse> createOrUpdateAsync(final PlaneRequest planeRequest) {
        return supplyAsync(() -> this.planeMapper.toEntity(planeRequest))
                .thenApplyAsync(this.planeRepository::save)
                .thenApplyAsync(this.planeMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String planeId) {
        return this.planeRepository.findById(Long.parseLong(planeId == null ? "0" : planeId))
                .map(
                        plane -> {
                            this.planeRepository.deleteById(plane.getId());
                            return PLANE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new PlaneNotFoundException(PLANE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String planeId) {
        return supplyAsync(() -> this.planeRepository.findById(Long.parseLong(planeId == null ? "0" : planeId)))
                .thenApplyAsync(
                        plane -> plane.stream()
                                .peek(foundPlane -> log.info(PlaneLogs.PLANE_FOUND_BEFORE_DELETE.getLogMessage(), foundPlane))
                                .map(
                                        foundPlane -> {
                                            this.planeRepository.deleteById(foundPlane.getId());
                                            return PLANE_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new PlaneNotFoundException(PLANE_NOT_FOUND.getMessage()))
                );
    }
}
