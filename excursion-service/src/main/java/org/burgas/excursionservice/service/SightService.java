package org.burgas.excursionservice.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionservice.dto.SightRequest;
import org.burgas.excursionservice.dto.SightResponse;
import org.burgas.excursionservice.entity.Image;
import org.burgas.excursionservice.exception.SightImageNotFoundException;
import org.burgas.excursionservice.exception.SightNotFoundException;
import org.burgas.excursionservice.exception.SightSaveOrTransformException;
import org.burgas.excursionservice.mapper.SightMapper;
import org.burgas.excursionservice.repository.SightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.log.SightLogs.*;
import static org.burgas.excursionservice.message.SightMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class SightService {

    private static final Logger log = LoggerFactory.getLogger(SightService.class);
    private final SightRepository sightRepository;
    private final SightMapper sightMapper;
    private final ImageService imageService;

    public SightService(SightRepository sightRepository, SightMapper sightMapper, ImageService imageService) {
        this.sightRepository = sightRepository;
        this.sightMapper = sightMapper;
        this.imageService = imageService;
    }

    public Set<SightResponse> findAll() {
        return this.sightRepository.findAll()
                .stream()
                .peek(sight -> log.info(SIGHT_FOUND_ALL.getLogMessage(), sight))
                .map(this.sightMapper::toSightResponse)
                .collect(Collectors.toSet());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Set<SightResponse>> findAllAsync() {
        return supplyAsync(this.sightRepository::findAll)
                .thenApplyAsync(
                        sights -> sights.stream()
                                .peek(sight -> log.info(SIGHT_FOUND_ALL_ASYNC.getLogMessage(), sight))
                                .map(this.sightMapper::toSightResponse)
                                .collect(Collectors.toSet())
                );
    }

    public SightResponse findById(final String sightId) {
        return this.sightRepository.findById(Long.valueOf(sightId))
                .stream()
                .peek(sight -> log.info(SIGHT_FOUND_BY_ID.getLogMessage(), sight))
                .map(this.sightMapper::toSightResponse)
                .findFirst()
                .orElseGet(SightResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<SightResponse> findByIdAsync(final String sightId) {
        return supplyAsync(() -> this.sightRepository.findById(Long.valueOf(sightId)))
                .thenApplyAsync(
                        sight -> sight.stream()
                                .peek(foundSight -> log.info(SIGHT_FOUND_BY_ID_ASYNC.getLogMessage(), foundSight))
                                .map(this.sightMapper::toSightResponse)
                                .findFirst()
                                .orElseGet(SightResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public SightResponse createOrUpdate(final SightRequest sightRequest) {
        return ofNullable(this.sightMapper.toSight(sightRequest))
                .map(this.sightRepository::save)
                .map(this.sightMapper::toSightResponse)
                .orElseThrow(() -> new SightSaveOrTransformException(SIGHT_SAVE_OR_TRANSFORM.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<SightResponse> createOrUpdateAsync(final SightRequest sightRequest) {
        return supplyAsync(() -> this.sightMapper.toSight(sightRequest))
                .thenApplyAsync(this.sightRepository::save)
                .thenApplyAsync(this.sightMapper::toSightResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String sightId) {
        return this.sightRepository.findById(Long.valueOf(sightId))
                .map(
                        sight -> {
                            this.sightRepository.deleteById(sight.getId());
                            return SIGHT_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String sightId) {
        return supplyAsync(() -> this.sightRepository.findById(Long.valueOf(sightId)))
                .thenApplyAsync(
                        sight -> sight.map(
                                foundSight -> {
                                    this.sightRepository.deleteById(foundSight.getId());
                                    return SIGHT_DELETED_ASYNC.getMessage();
                                }
                        )
                                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String sightId, final Part part) {
        return this.sightRepository.findById(Long.valueOf(sightId))
                .map(
                        sight -> {
                            Image image = this.imageService.uploadImage(part);
                            sight.setImageId(image.getId());
                            this.sightRepository.save(sight);
                            return format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String sightId, final Part part) {
        return supplyAsync(() -> this.sightRepository.findById(Long.valueOf(sightId)))
                .thenApplyAsync(sight -> sight.map(
                        foundSight -> {
                            try {
                                Image image = this.imageService.uploadImageAsync(part).get();
                                foundSight.setImageId(image.getId());
                                return format(IMAGE_UPLOADED_ASYNC.getMessage(), image.getId());

                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                        .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String sightId, final Part part) {
        return this.sightRepository.findById(Long.valueOf(sightId))
                .map(
                        sight -> of(sight.getImageId())
                                .map(imageId -> {
                                    Image image = this.imageService.changeImage(String.valueOf(imageId), part);
                                    return format(IMAGE_CHANGED.getMessage(), image.getId());
                                })
                                .orElseThrow(() -> new SightImageNotFoundException(SIGHT_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String sightId, final Part part) {
        return supplyAsync(() -> this.sightRepository.findById(Long.valueOf(sightId)))
                .thenApplyAsync(
                        sight -> sight.map(
                                foundSight -> of(foundSight.getImageId())
                                        .map(
                                                imageId -> {
                                                    try {
                                                        Image image = this.imageService.changeImageAsync(String.valueOf(imageId), part).get();
                                                        return format(IMAGE_CHANGED_ASYNC.getMessage(), image.getId());

                                                    } catch (InterruptedException | ExecutionException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        )
                                        .orElseThrow(() -> new SightImageNotFoundException(SIGHT_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String sightId) {
        return this.sightRepository.findById(Long.valueOf(sightId))
                .map(
                        sight -> of(sight.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new SightImageNotFoundException(SIGHT_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String sightId) {
        return supplyAsync(() -> this.sightRepository.findById(Long.valueOf(sightId)))
                .thenApplyAsync(
                        sight -> sight.map(
                                foundSight -> of(foundSight.getImageId())
                                        .map(
                                                imageId -> {
                                                    try {
                                                        this.imageService.deleteImageAsync(String.valueOf(imageId)).get();
                                                        return format(IMAGE_DELETED_ASYNC.getMessage(), imageId);

                                                    } catch (InterruptedException | ExecutionException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        )
                                        .orElseThrow(() -> new SightImageNotFoundException(SIGHT_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new SightNotFoundException(SIGHT_NOT_FOUND.getMessage()))
                );
    }
}
