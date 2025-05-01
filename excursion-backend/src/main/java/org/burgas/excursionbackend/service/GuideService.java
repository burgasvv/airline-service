package org.burgas.excursionbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.dto.GuideRequest;
import org.burgas.excursionbackend.dto.GuideResponse;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.exception.GuideImageNotFoundException;
import org.burgas.excursionbackend.exception.GuideNotFoundException;
import org.burgas.excursionbackend.mapper.GuideMapper;
import org.burgas.excursionbackend.repository.GuideRepository;
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
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.GuideLogs.*;
import static org.burgas.excursionbackend.message.GuideMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class GuideService {

    private static final Logger log = LoggerFactory.getLogger(GuideService.class);
    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;
    private final ImageService imageService;

    public GuideService(GuideRepository guideRepository, GuideMapper guideMapper, ImageService imageService) {
        this.guideRepository = guideRepository;
        this.guideMapper = guideMapper;
        this.imageService = imageService;
    }

    public List<GuideResponse> findAll() {
        return this.guideRepository.findAll()
                .stream()
                .peek(guide -> log.info(GUIDE_FOUND_ALL.getLogMessage(), guide))
                .map(this.guideMapper::toGuideResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<GuideResponse>> findAllAsync() {
        return supplyAsync(this.guideRepository::findAll)
                .thenApplyAsync(
                        guides -> guides.stream()
                                .peek(guide -> log.info(GUIDE_FOUND_ALL_ASYNC.getLogMessage(), guide))
                                .map(this.guideMapper::toGuideResponse)
                                .toList()
                );
    }

    public Page<GuideResponse> findAllPages(final Integer page, final Integer size) {
        return this.guideRepository.findAll(PageRequest.of(page - 1, size)
                        .withSort(Sort.Direction.ASC, "name", "surname", "patronymic"))
                .map(this.guideMapper::toGuideResponse);
    }

    public GuideResponse findById(final String guideId) {
        return this.guideRepository.findById(Long.valueOf(guideId))
                .stream()
                .peek(guide -> log.info(GUIDE_FOUND_BY_ID.getLogMessage(), guide))
                .map(this.guideMapper::toGuideResponse)
                .findFirst()
                .orElseGet(GuideResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<GuideResponse> findByIdAsync(final String guideId) {
        return supplyAsync(() -> this.guideRepository.findById(Long.valueOf(guideId)))
                .thenApplyAsync(
                        guide -> guide.stream()
                                .peek(logGuide -> log.info(GUIDE_FOUND_BY_ID_ASYNC.getLogMessage(), logGuide))
                                .map(this.guideMapper::toGuideResponse)
                                .findFirst()
                                .orElseGet(GuideResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public GuideResponse createOrUpdate(final GuideRequest guideRequest) {
        return ofNullable(this.guideMapper.toGuideSave(guideRequest))
                .map(this.guideMapper::toGuideResponse)
                .orElseGet(GuideResponse::new);
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<GuideResponse> createOrUpdateAsync(final GuideRequest guideRequest) {
        return supplyAsync(() -> this.guideMapper.toGuideSave(guideRequest))
                .thenApplyAsync(this.guideMapper::toGuideResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String guideId) {
        return this.guideRepository.findById(Long.valueOf(guideId))
                .map(
                        guide -> {
                            this.guideRepository.deleteById(guide.getId());
                            return format(GUIDE_DELETED.getMessage(), guide.getId());
                        }
                )
                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String guideId) {
        return supplyAsync(() -> this.guideRepository.findById(Long.valueOf(guideId)))
                .thenApplyAsync(
                        guide -> guide.map(
                                foundGuide -> {
                                    this.guideRepository.deleteById(foundGuide.getId());
                                    return format(GUIDE_DELETED.getMessage(), foundGuide.getId());
                                }
                        )
                                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String guideId, final Part part) {
        return this.guideRepository.findById(Long.valueOf(guideId))
                .map(
                        guide -> {
                            Image image = this.imageService.uploadImage(part);
                            guide.setImageId(image.getId());
                            this.guideRepository.save(guide);
                            return String.format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String guideId, final Part part) {
        return supplyAsync(() -> this.guideRepository.findById(Long.valueOf(guideId)))
                .thenApplyAsync(
                        guide -> guide.map(
                                foundGuide -> {
                                    try {
                                        Image image = this.imageService.uploadImageAsync(part).get();
                                        foundGuide.setImageId(image.getId());
                                        this.guideRepository.save(foundGuide);
                                        return format(IMAGE_UPLOADED_ASYNC.getMessage(), image.getId());

                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String guideId, final Part part) {
        return this.guideRepository.findById(Long.valueOf(guideId))
                .map(
                        guide -> of(guide.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(String.valueOf(imageId), part);
                                            return format(IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(() -> new GuideImageNotFoundException(GUIDE_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String guideId, final Part part) {
        return supplyAsync(() -> this.guideRepository.findById(Long.valueOf(guideId)))
                .thenApplyAsync(
                        guide -> guide.map(
                                foundGuide -> of(foundGuide.getImageId())
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
                                        .orElseThrow(() -> new GuideImageNotFoundException((GUIDE_IMAGE_NOT_FOUND.getMessage())))
                        )
                                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String guideId) {
        return this.guideRepository.findById(Long.valueOf(guideId))
                .map(
                        guide -> of(guide.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new GuideImageNotFoundException(GUIDE_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String guideId) {
        return supplyAsync(() -> this.guideRepository.findById(Long.valueOf(guideId)))
                .thenApplyAsync(
                        guide -> guide.map(
                                foundGuide -> of(foundGuide.getImageId())
                                        .map(
                                                imageId -> {
                                                    this.imageService.deleteImageAsync(String.valueOf(imageId));
                                                    return format(IMAGE_DELETED_ASYNC.getMessage(), imageId);
                                                }
                                        )
                                        .orElseThrow(() -> new GuideImageNotFoundException(GUIDE_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new GuideNotFoundException(GUIDE_NOT_FOUND.getMessage()))
                );
    }
}
