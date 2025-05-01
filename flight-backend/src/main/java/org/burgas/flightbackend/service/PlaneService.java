package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.PlaneRequest;
import org.burgas.flightbackend.dto.PlaneResponse;
import org.burgas.flightbackend.exception.PlaneNotCreateException;
import org.burgas.flightbackend.log.PlaneLogs;
import org.burgas.flightbackend.mapper.PlaneMapper;
import org.burgas.flightbackend.repository.PlaneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.burgas.flightbackend.log.PlaneLogs.PLANE_FOUND_ALL;
import static org.burgas.flightbackend.message.PlaneMessages.PLANE_NOT_CREATED;
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
                .map(this.planeMapper::toPlaneResponse)
                .collect(Collectors.toList());
    }

    public List<PlaneResponse> findAllByFree(final String free) {
        return this.planeRepository.findPlanesByFree(Boolean.parseBoolean(free))
                .stream()
                .peek(plane -> log.info(PlaneLogs.PLANE_FOUND_BY_FREE.getLogMessage(), plane))
                .map(this.planeMapper::toPlaneResponse)
                .collect(Collectors.toList());
    }

    public PlaneResponse findById(final String planeId) {
        return this.planeRepository.findById(Long.parseLong(planeId))
                .stream()
                .peek(plane -> log.info(PlaneLogs.PLANE_FOUND_BY_ID.getLogMessage(), plane))
                .map(this.planeMapper::toPlaneResponse)
                .findFirst()
                .orElseGet(PlaneResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PlaneResponse createOrUpdate(final PlaneRequest planeRequest) {
        return of(this.planeMapper.toPlane(planeRequest))
                .map(this.planeRepository::save)
                .map(this.planeMapper::toPlaneResponse)
                .orElseThrow(
                        () -> new PlaneNotCreateException(PLANE_NOT_CREATED.getMessage())
                );
    }
}
