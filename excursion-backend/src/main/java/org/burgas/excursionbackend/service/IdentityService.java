package org.burgas.excursionbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.dto.IdentityRequest;
import org.burgas.excursionbackend.dto.IdentityResponse;
import org.burgas.excursionbackend.entity.Identity;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.exception.IdentityImageNotFoundException;
import org.burgas.excursionbackend.exception.IdentityNotFoundException;
import org.burgas.excursionbackend.mapper.IdentityMapper;
import org.burgas.excursionbackend.repository.IdentityRepository;
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
import static java.lang.String.valueOf;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.IdentityLogs.*;
import static org.burgas.excursionbackend.log.ImageLogs.IMAGE_WAS_CREATED;
import static org.burgas.excursionbackend.log.ImageLogs.IMAGE_WAS_CREATED_ASYNC;
import static org.burgas.excursionbackend.message.IdentityMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final ImageService imageService;

    public IdentityService(IdentityRepository identityRepository, IdentityMapper identityMapper, ImageService imageService) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.imageService = imageService;
    }

    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_OF_ALL.getLogMessage(), identity))
                .map(this.identityMapper::toResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<IdentityResponse>> findAllAsync() {
        return supplyAsync(this.identityRepository::findAll)
                .thenApplyAsync(
                        identities -> identities.stream()
                                .peek(identity -> log.info(IDENTITY_FOUND_OF_ALL_ASYNC.getLogMessage(), identity))
                                .map(this.identityMapper::toResponse)
                                .toList()
                );
    }

    public Page<IdentityResponse> findAllPages(final Integer page, final Integer size) {
        return this.identityRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "username"))
                .map(this.identityMapper::toResponse);
    }

    public List<IdentityResponse> findAllByExcursionId(final String excursionId) {
        return this.identityRepository.findIdentitiesByExcursionId(Long.valueOf(excursionId))
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_OF_ALL_BY_EXCURSION_ID.getLogMessage(), identity))
                .map(this.identityMapper::toResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<IdentityResponse>> findAllByExcursionIdAsync(final String excursionId) {
        return supplyAsync(() -> this.identityRepository.findIdentitiesByExcursionId(Long.valueOf(excursionId)))
                .thenApplyAsync(
                        identities -> identities.stream()
                                .peek(identity -> log.info(IDENTITY_FOUND_OF_ALL_BY_EXCURSION_ID_ASYNC.getLogMessage(), identity))
                                .map(this.identityMapper::toResponse)
                                .toList()
                );
    }

    public IdentityResponse findById(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_ID.getLogMessage(), identity))
                .map(this.identityMapper::toResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    public IdentityResponse findByIdOrElseThrow(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_ID.getLogMessage(), identity))
                .map(this.identityMapper::toResponse)
                .findFirst()
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<IdentityResponse> findByIdAsync(final String identityId) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BY_ID_ASYNC.getLogMessage(), foundIdentity))
                                .map(this.identityMapper::toResponse)
                                .findFirst()
                )
                .thenApplyAsync(identityResponse -> identityResponse.orElseGet(IdentityResponse::new));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(final IdentityRequest identityRequest) {
        return ofNullable(this.identityMapper.toEntity(identityRequest))
                .map(this.identityRepository::save)
                .stream()
                .peek(identity -> log.info(IDENTITY_SAVED.getLogMessage(), identity))
                .map(this.identityMapper::toResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<IdentityResponse> createOrUpdateAsync(final IdentityRequest identityRequest) {
        return supplyAsync(() -> this.identityMapper.toEntity(identityRequest))
                .thenApplyAsync(this.identityRepository::save)
                .thenApplyAsync(this.identityMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            log.info(IDENTITY_FOUND_BEFORE_DELETE.getLogMessage(), identity);
                            this.identityRepository.deleteById(identity.getId());
                            return IDENTITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String identityId) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity
                                .map(
                                        foundIdentity -> {
                                            log.info(IDENTITY_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundIdentity);
                                            this.identityRepository.deleteById(foundIdentity.getId());
                                            return IDENTITY_DELETED.getMessage();
                                        }
                                )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String accountControl(final String identityId, final String enable) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            log.info(IDENTITY_FOUND_CONTROL.getLogMessage(), identity);
                            identity.setEnabled(Boolean.valueOf(enable));
                            Identity saved = this.identityRepository.save(identity);
                            return saved.getEnabled() ? IDENTITY_ENABLED.getMessage() : IDENTITY_NOT_ENABLED.getMessage();
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> accountControlAsync(final String identityId, final String enable) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.map(
                                        foundIdentity -> {
                                            log.info(IDENTITY_FOUND_CONTROL_ASYNC.getLogMessage(), foundIdentity);
                                            foundIdentity.setEnabled(Boolean.valueOf(enable));
                                            return this.identityRepository.save(foundIdentity);
                                        }
                                )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                )
                .thenApplyAsync(identity -> identity.getEnabled() ? IDENTITY_ENABLED.getMessage() : IDENTITY_NOT_ENABLED.getMessage());
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String identityId, final Part part) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> {
                            Image image = this.imageService.uploadImage(part);
                            log.info(IMAGE_WAS_CREATED.getLogMessage(), image);
                            identity.setImageId(image.getId());
                            this.identityRepository.save(identity);
                            return format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String identityId, final Part part) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.map(
                                foundIdentity -> {
                                    try {
                                        Image image = this.imageService.uploadImageAsync(part).get();
                                        log.info(IMAGE_WAS_CREATED_ASYNC.getLogMessage());
                                        foundIdentity.setImageId(image.getId());
                                        this.identityRepository.save(foundIdentity);
                                        return format(IMAGE_UPLOADED_ASYNC.getMessage());

                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String identityId, final Part part) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(valueOf(imageId), part);
                                            return format(IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(() -> new IdentityImageNotFoundException(IDENTITY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String identityId, final Part part) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.map(
                                foundIdentity -> of(foundIdentity.getImageId())
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
                                        .orElseThrow(() -> new IdentityImageNotFoundException(IDENTITY_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String identityId) {
        return this.identityRepository.findById(Long.valueOf(identityId))
                .map(
                        identity -> of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new IdentityImageNotFoundException(IDENTITY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String identityId) {
        return supplyAsync(() -> this.identityRepository.findById(Long.valueOf(identityId)))
                .thenApplyAsync(
                        identity -> identity.map(
                                foundIdentity -> of(foundIdentity.getImageId())
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
                                        .orElseThrow(() -> new IdentityImageNotFoundException(IDENTITY_IMAGE_NOT_FOUND_ASYNC.getMessage()))
                        )
                                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
                );
    }
}
