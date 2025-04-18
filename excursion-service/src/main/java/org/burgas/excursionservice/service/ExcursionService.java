package org.burgas.excursionservice.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionservice.dto.ExcursionRequest;
import org.burgas.excursionservice.dto.ExcursionResponse;
import org.burgas.excursionservice.entity.Image;
import org.burgas.excursionservice.exception.ExcursionImageNotFoundException;
import org.burgas.excursionservice.exception.ExcursionNotFoundException;
import org.burgas.excursionservice.mapper.ExcursionMapper;
import org.burgas.excursionservice.repository.ExcursionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.log.ExcursionLogs.*;
import static org.burgas.excursionservice.message.ExcursionMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ExcursionService {

    private static final Logger log = LoggerFactory.getLogger(ExcursionService.class);
    private final ExcursionRepository excursionRepository;
    private final ExcursionMapper excursionMapper;
    private final ImageService imageService;

    public ExcursionService(ExcursionRepository excursionRepository, ExcursionMapper excursionMapper, ImageService imageService) {
        this.excursionRepository = excursionRepository;
        this.excursionMapper = excursionMapper;
        this.imageService = imageService;
    }

    public List<ExcursionResponse> findAll() {
        return this.excursionRepository.findAll()
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_ALL.getLogMessage(), excursion))
                .map(this.excursionMapper::toExcursionResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<ExcursionResponse>> findAllAsync() {
        return supplyAsync(this.excursionRepository::findAll)
                .thenApplyAsync(
                        excursions -> excursions.stream()
                                .peek(excursion -> log.info(EXCURSION_FOUND_ALL_ASYNC.getLogMessage(), excursion))
                                .map(this.excursionMapper::toExcursionResponse)
                                .toList()
                );
    }

    public List<ExcursionResponse> findAllByGuideId(final String guideId) {
        return this.excursionRepository.findExcursionsByGuideId(Long.valueOf(guideId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_ALL_GUIDE_ID.getLogMessage(), excursion))
                .map(this.excursionMapper::toExcursionResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<ExcursionResponse>> findAllByGuideIdAsync(final String guideId) {
        return supplyAsync(() -> this.excursionRepository.findExcursionsByGuideId(Long.valueOf(guideId)))
                .thenApplyAsync(
                        excursions -> excursions.stream()
                                .peek(excursion -> log.info(EXCURSION_FOUND_ALL_GUIDE_ID_ASYNC.getLogMessage(), excursion))
                                .map(this.excursionMapper::toExcursionResponse)
                                .toList()
                );
    }

    public ExcursionResponse findById(final String excursionId) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_BY_ID.getLogMessage(), excursion))
                .map(this.excursionMapper::toExcursionResponse)
                .findFirst()
                .orElseGet(ExcursionResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<ExcursionResponse> findByIdAsync(final String excursionId) {
        return supplyAsync(() -> excursionRepository.findById(Long.valueOf(excursionId)))
                .thenApplyAsync(
                        excursion -> excursion.stream()
                                .peek(logExcursion -> log.info(EXCURSION_BY_ID_ASYNC.getLogMessage(), logExcursion))
                                .map(this.excursionMapper::toExcursionResponse)
                                .findFirst()
                                .orElseGet(ExcursionResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public ExcursionResponse createOrUpdate(final ExcursionRequest excursionRequest) {
        return of(this.excursionMapper.toExcursionSave(excursionRequest))
                .map(this.excursionRepository::save)
                .stream()
                .peek(excursion -> log.info(EXCURSION_SAVED.getLogMessage(), excursion))
                .map(this.excursionMapper::toExcursionResponse)
                .findFirst()
                .orElseGet(ExcursionResponse::new);
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<ExcursionResponse> createOrUpdateAsync(final ExcursionRequest excursionRequest) {
        return supplyAsync(() -> this.excursionMapper.toExcursionSave(excursionRequest))
                .thenApplyAsync(
                        excursion -> of(excursion)
                                .map(this.excursionRepository::save)
                                .stream()
                                .peek(savedExcursion -> log.info(EXCURSION_SAVED_ASYNC.getLogMessage(), savedExcursion))
                                .map(this.excursionMapper::toExcursionResponse)
                                .findFirst()
                                .orElseGet(ExcursionResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String excursionId) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_DELETE.getLogMessage(), excursion))
                .map(
                        excursion -> {
                            this.excursionRepository.deleteById(excursion.getId());
                            return EXCURSION_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String excursionId) {
        return supplyAsync(() -> this.excursionRepository.findById(Long.valueOf(excursionId)))
                .thenApplyAsync(
                        excursion -> excursion.stream()
                                .peek(logExcursion -> log.info(EXCURSION_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), logExcursion))
                                .map(
                                        deleteExcursion -> {
                                            this.excursionRepository.deleteById(deleteExcursion.getId());
                                            return EXCURSION_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String excursionId, final Part part) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_UPLOAD_IMAGE.getLogMessage(), excursion))
                .map(
                        excursion -> {
                            Image image = this.imageService.uploadImage(part);
                            excursion.setImageId(image.getId());
                            this.excursionRepository.save(excursion);
                            return format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .findFirst()
                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String excursionId, final Part part) {
        return supplyAsync(() -> this.excursionRepository.findById(Long.valueOf(excursionId)))
                .thenApplyAsync(
                        excursion -> excursion.stream()
                                .peek(logExcursion -> log.info(EXCURSION_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC.getLogMessage(), logExcursion))
                                .map(
                                        foundExcursion -> {
                                            try {
                                                Image image = this.imageService.uploadImageAsync(part).get();
                                                foundExcursion.setImageId(image.getId());
                                                this.excursionRepository.save(foundExcursion);
                                                return format(IMAGE_UPLOADED_ASYNC.getMessage(), image.getId());

                                            } catch (InterruptedException | ExecutionException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String excursionId, final Part part) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_CHANGE_IMAGE.getLogMessage(), excursion))
                .map(
                        excursion -> of(excursion.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(String.valueOf(imageId), part);
                                            return format(IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(() -> new ExcursionImageNotFoundException(EXCURSION_IMAGE_NOT_FOUND.getMessage()))
                )
                .findFirst()
                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String excursionId, final Part part) {
        return supplyAsync(() -> this.excursionRepository.findById(Long.valueOf(excursionId)))
                .thenApplyAsync(
                        excursion -> excursion.stream()
                                .peek(logExcursion -> log.info(EXCURSION_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), logExcursion))
                                .map(
                                        foundExcursion -> of(foundExcursion.getImageId())
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
                                                .orElseThrow(() -> new ExcursionImageNotFoundException(EXCURSION_IMAGE_NOT_FOUND.getMessage()))
                                )
                                .findFirst()
                                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String excursionId) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_DELETE_IMAGE.getLogMessage(), excursion))
                .map(
                        excursion -> of(excursion.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new ExcursionImageNotFoundException(EXCURSION_IMAGE_NOT_FOUND.getMessage()))
                )
                .findFirst()
                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String executorId) {
        return supplyAsync(() -> this.excursionRepository.findById(Long.valueOf(executorId)))
                .thenApplyAsync(
                        excursion -> excursion.stream()
                                .peek(logExcursion -> log.info(EXCURSION_FOUND_BEFORE_DELETE_IMAGE_ASYNC.getLogMessage(), logExcursion))
                                .map(
                                        foundExcursion -> of(foundExcursion.getImageId())
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
                                                .orElseThrow(() -> new ExcursionImageNotFoundException(EXCURSION_IMAGE_NOT_FOUND.getMessage()))
                                )
                                .findFirst()
                                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()))
                );
    }
}
