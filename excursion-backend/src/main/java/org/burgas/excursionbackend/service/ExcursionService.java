package org.burgas.excursionbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.dto.ExcursionRequest;
import org.burgas.excursionbackend.dto.ExcursionResponse;
import org.burgas.excursionbackend.entity.ExcursionIdentity;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.exception.ExcursionAlreadyExistsByIdentityException;
import org.burgas.excursionbackend.exception.ExcursionImageNotFoundException;
import org.burgas.excursionbackend.exception.ExcursionNotFoundException;
import org.burgas.excursionbackend.exception.ExcursionPassedException;
import org.burgas.excursionbackend.mapper.ExcursionMapper;
import org.burgas.excursionbackend.repository.ExcursionIdentityRepository;
import org.burgas.excursionbackend.repository.ExcursionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.excursionbackend.log.ExcursionLogs.*;
import static org.burgas.excursionbackend.message.ExcursionMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
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
    private final ExcursionIdentityRepository excursionIdentityRepository;

    public ExcursionService(
            ExcursionRepository excursionRepository, ExcursionMapper excursionMapper,
            ImageService imageService, ExcursionIdentityRepository excursionIdentityRepository
    ) {
        this.excursionRepository = excursionRepository;
        this.excursionMapper = excursionMapper;
        this.imageService = imageService;
        this.excursionIdentityRepository = excursionIdentityRepository;
    }

    @Scheduled(timeUnit = SECONDS, fixedRate = 50)
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void checkExcursionSchedule() {
        this.excursionRepository.findExcursionsByPassed(false).forEach(
                excursion -> {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime starts = excursion.getStarts();
                    LocalDateTime ends = excursion.getEnds();

                    if (now.isBefore(starts)) {
                        excursion.setPassed(false);
                        excursion.setInProgress(false);
                        this.excursionRepository.save(excursion);

                    } else if (now.isAfter(starts) && now.isBefore(ends)) {
                        excursion.setPassed(false);
                        excursion.setInProgress(true);
                        this.excursionRepository.save(excursion);

                    } else if (now.isAfter(starts) && now.isAfter(ends)) {
                        excursion.setPassed(true);
                        excursion.setInProgress(false);
                        this.excursionRepository.save(excursion);
                    }
                }
        );
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

    public List<ExcursionResponse> findAllByIdentityId(final String identityId) {
        return this.excursionRepository.findExcursionsByIdentityId(Long.valueOf(identityId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_ALL_IDENTITY_ID.getLogMessage(), excursion))
                .map(this.excursionMapper::toExcursionResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<ExcursionResponse>> findAllByIdentityIdAsync(final String identityId) {
        return supplyAsync(() -> this.excursionRepository.findExcursionsByIdentityId(Long.valueOf(identityId)))
                .thenApplyAsync(
                        excursions -> excursions.stream()
                                .peek(excursion -> log.info(EXCURSION_FOUND_ALL_IDENTITY_ID_ASYNC.getLogMessage(), excursion))
                                .map(this.excursionMapper::toExcursionResponse)
                                .toList()
                );
    }

    @SuppressWarnings("unchecked")
    public List<ExcursionResponse> findAllBySession(final HttpServletRequest httpServletRequest) {
        return ofNullable((List<ExcursionResponse>) httpServletRequest.getSession().getAttribute("session-excursions"))
                .orElseGet(ArrayList::new);
    }

    @Async(value = "taskExecutor")
    @SuppressWarnings("unchecked")
    public CompletableFuture<List<ExcursionResponse>> findAllBySessionAsync(final HttpServletRequest httpServletRequest) {
        return supplyAsync(
                () -> ofNullable((List<ExcursionResponse>) httpServletRequest.getSession().getAttribute("session-excursions"))
                    .orElseGet(ArrayList::new)
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
    public String addExcursionByIdentityId(final String excursionId, final String identityId) {
        return ofNullable(
                this.excursionIdentityRepository
                        .existsExcursionIdentityByExcursionIdAndIdentityId(Long.valueOf(excursionId), Long.valueOf(identityId))
        )
                .filter(exists -> !exists)
                .map(
                        _ -> this.excursionRepository.findById(Long.valueOf(excursionId))
                                .stream()
                                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_ADDING_BY_IDENTITY.getLogMessage(), excursion))
                                .filter(excursion -> !excursion.getPassed())
                                .map(
                                        excursion -> {
                                            this.excursionIdentityRepository.save(
                                                    ExcursionIdentity.builder()
                                                            .excursionId(excursion.getId())
                                                            .identityId(Long.valueOf(identityId))
                                                            .build()
                                            );
                                            return EXCURSION_ADDED_BY_IDENTITY.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new ExcursionAlreadyExistsByIdentityException(EXCURSION_ALREADY_EXISTS_BY_IDENTITY.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> addExcursionByIdentityIdAsync(final String excursionId, final String identityId) {
        return supplyAsync(() -> this.addExcursionByIdentityId(excursionId, identityId));
    }

    public String addExcursionToSession(final String excursionId, final HttpServletRequest httpServletRequest) {
        return this.excursionRepository.findById(Long.valueOf(excursionId))
                .stream()
                .peek(excursion -> log.info(EXCURSION_FOUND_BEFORE_ADDING_TO_SESSION.getLogMessage(), excursion))
                .map(
                        excursion -> of(excursion)
                                .filter(checkExcursion -> !checkExcursion.getPassed())
                                .orElseThrow(() -> new ExcursionPassedException(EXCURSION_PASSED.getMessage()))
                )
                .map(this.excursionMapper::toExcursionResponse)
                .map(
                        excursionResponse -> {
                            HttpSession session = httpServletRequest.getSession();
                            @SuppressWarnings("unchecked")
                            List<ExcursionResponse> excursionResponses = (List<ExcursionResponse>) session.getAttribute("session-excursions");
                            if (excursionResponses != null && !excursionResponses.isEmpty()) {
                                excursionResponses.add(excursionResponse);
                                session.setAttribute("session-excursions", excursionResponses);
                            } else {
                                List<ExcursionResponse> newExcursionResponses = new ArrayList<>();
                                newExcursionResponses.add(excursionResponse);
                                session.setAttribute("session-excursions", newExcursionResponses);
                            }
                            return EXCURSION_ADDED_TO_SESSION.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(() -> new ExcursionNotFoundException(EXCURSION_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<String> addExcursionToSessionAsync(final String excursionId, final HttpServletRequest httpServletRequest) {
        return supplyAsync(
                () -> this.excursionRepository.findById(Long.valueOf(excursionId))
                        .stream()
                        .peek(logExcursion -> log.info(EXCURSION_FOUND_BEFORE_ADDING_TO_SESSION_ASYNC.getLogMessage(), logExcursion))
                        .map(
                                excursion -> of(excursion)
                                        .filter(checkExcursion -> !checkExcursion.getPassed())
                                        .orElseThrow(() -> new ExcursionPassedException(EXCURSION_PASSED.getMessage()))
                        )
        )
                .thenApplyAsync(
                        excursion -> excursion
                                .map(this.excursionMapper::toExcursionResponse)
                                .map(
                                        excursionResponse -> {
                                            HttpSession session = httpServletRequest.getSession();
                                            @SuppressWarnings("unchecked")
                                            List<ExcursionResponse> excursionResponses = (List<ExcursionResponse>)
                                                    session.getAttribute("session-excursions");
                                            if (excursionResponses != null && !excursionResponses.isEmpty()) {
                                                excursionResponses.add(excursionResponse);
                                                session.setAttribute("session-excursions", excursionResponses);
                                            } else {
                                                List<ExcursionResponse> newExcursionResponses = new ArrayList<>();
                                                newExcursionResponses.add(excursionResponse);
                                                session.setAttribute("session-excursions", newExcursionResponses);
                                            }
                                            return EXCURSION_ADDED_TO_SESSION_ASYNC.getMessage();
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
