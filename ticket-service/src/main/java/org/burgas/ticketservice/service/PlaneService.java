package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.PlaneRequest;
import org.burgas.ticketservice.dto.PlaneResponse;
import org.burgas.ticketservice.mapper.PlaneMapper;
import org.burgas.ticketservice.repository.PlaneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PlaneService {

    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;

    public PlaneService(PlaneRepository planeRepository, PlaneMapper planeMapper) {
        this.planeRepository = planeRepository;
        this.planeMapper = planeMapper;
    }

    public List<PlaneResponse> findAll() {
        return this.planeRepository.findAll()
                .stream()
                .map(this.planeMapper::toPlaneResponse)
                .toList();
    }

    public List<PlaneResponse> findAllByFree(final String free) {
        return this.planeRepository.findPlanesByFree(Boolean.parseBoolean(free))
                .stream()
                .map(this.planeMapper::toPlaneResponse)
                .toList();
    }

    public PlaneResponse findById(final String planeId) {
        return this.planeRepository.findById(Long.parseLong(planeId))
                .map(this.planeMapper::toPlaneResponse)
                .orElseGet(PlaneResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PlaneResponse createOrUpdate(final PlaneRequest planeRequest) {
        return Optional.of(this.planeMapper.toPlane(planeRequest))
                .map(this.planeRepository::save)
                .map(this.planeMapper::toPlaneResponse)
                .orElseGet(PlaneResponse::new);
    }
}
