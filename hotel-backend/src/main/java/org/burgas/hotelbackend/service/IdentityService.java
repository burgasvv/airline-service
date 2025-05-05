package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.IdentityRequest;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.entity.Identity;
import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.IdentityNotCreatedException;
import org.burgas.hotelbackend.exception.IdentityNotFoundException;
import org.burgas.hotelbackend.exception.IdentityStatusAlreadySetException;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.mapper.IdentityMapper;
import org.burgas.hotelbackend.repository.IdentityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.IdentityLogs.*;
import static org.burgas.hotelbackend.message.IdentityMessages.*;
import static org.burgas.hotelbackend.message.ImageMessages.IMAGE_NOT_FOUND;
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
                .peek(identity -> log.info(IDENTITY_FOUND_ALL.getLogMessage(), identity))
                .map(this.identityMapper::toIdentityResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<IdentityResponse>> findAllAsync() {
        return supplyAsync(this.identityRepository::findAll)
                .thenApplyAsync(
                        identities -> identities.stream()
                                .peek(identity -> log.info(IDENTITY_FOUND_ALL_ASYNC.getLogMessage(), identity))
                                .map(this.identityMapper::toIdentityResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<IdentityResponse> findAllPages(final Integer page, final Integer size) {
        return this.identityRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "username"))
                .map(this.identityMapper::toIdentityResponse);
    }

    public IdentityResponse findById(final Long identityId) {
        return this.identityRepository.findById(identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_ID.getLogMessage(), identity))
                .map(this.identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(IdentityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<IdentityResponse> findByIdAsync(final Long identityId) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BY_ID_ASYNC.getLogMessage(), foundIdentity))
                                .map(this.identityMapper::toIdentityResponse)
                                .findFirst()
                                .orElseGet(IdentityResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(final IdentityRequest identityRequest) {
        return of(this.identityMapper.toIdentity(identityRequest))
                .map(this.identityRepository::save)
                .map(this.identityMapper::toIdentityResponse)
                .orElseThrow(
                        () -> new IdentityNotCreatedException(IDENTITY_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<IdentityResponse> createOrUpdateAsync(final IdentityRequest identityRequest) {
        return supplyAsync(() -> this.identityMapper.toIdentity(identityRequest))
                .thenApplyAsync(this.identityRepository::save)
                .thenApplyAsync(this.identityMapper::toIdentityResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String activateOrDeactivate(final Long identityId, final Boolean enable) {
        return this.identityRepository.findById(identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_ACTIVATE_DEACTIVATE.getLogMessage(), identity))
                .map(
                        identity -> {
                            if (identity.getEnabled().equals(enable))
                                throw new IdentityStatusAlreadySetException(IDENTITY_STATUS_ALREADY_SET.getMessage());

                            identity.setEnabled(enable);
                            this.identityRepository.save(identity);
                            return enable ? IDENTITY_ACTIVATED.getMessage() : IDENTITY_DEACTIVATED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> activateOrDeactivateAsync(final Long identityId, final Boolean enable) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BEFORE_ACTIVATE_DEACTIVATE_ASYNC.getLogMessage(), foundIdentity))
                                .map(
                                        foundIdentity -> {
                                            if (foundIdentity.getEnabled().equals(enable))
                                                throw new IdentityStatusAlreadySetException(IDENTITY_STATUS_ALREADY_SET.getMessage());

                                            foundIdentity.setEnabled(enable);
                                            this.identityRepository.save(foundIdentity);
                                            return enable ? IDENTITY_ACTIVATED.getMessage() : IDENTITY_DEACTIVATED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadIdentityImage(final Long identityId, final MultipartFile multipartFile) {
        return this.identityRepository.findById(identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_UPLOADING_IMAGE.getLogMessage(), identity))
                .map(
                        identity -> {
                            Image image = this.imageService.uploadImage(multipartFile);
                            identity.setImageId(image.getId());
                            Identity saved = this.identityRepository.save(identity);
                            return format(IDENTITY_IMAGE_UPLOADED.getMessage(), image.getId(), saved.getId());
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadIdentityImageAsync(final Long identityId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BEFORE_UPLOADING_IMAGE_ASYNC.getLogMessage(), foundIdentity))
                                .map(
                                        foundIdentity -> {
                                            try {
                                                Image image = this.imageService.uploadImageAsync(multipartFile).get();
                                                foundIdentity.setImageId(image.getId());
                                                Identity saved = this.identityRepository.save(foundIdentity);
                                                return format(IDENTITY_IMAGE_UPLOADED_ASYNC.getMessage(), image.getId(), saved.getId());

                                            } catch (InterruptedException | ExecutionException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeIdentityImage(final Long identityId, final MultipartFile multipartFile) {
        return this.identityRepository.findById(identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_CHANGING_IMAGE.getLogMessage(), identity))
                .map(
                        identity -> Optional.of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(imageId, multipartFile);
                                            return format(IDENTITY_IMAGE_CHANGED.getMessage(), image.getId(), identity.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeIdentityImageAsync(final Long identityId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BEFORE_CHANGING_IMAGE_ASYNC.getLogMessage(), foundIdentity))
                                .findFirst()
                                .map(
                                        foundIdentity -> Optional.of(foundIdentity.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                Image image = this.imageService.changeImageAsync(imageId, multipartFile).get();
                                                                return format(IDENTITY_IMAGE_CHANGED_ASYNC.getMessage(), image.getId(), foundIdentity.getId());

                                                            } catch (InterruptedException | ExecutionException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .orElseThrow(
                                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteIdentityImage(final Long identityId) {
        return this.identityRepository.findById(identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_DELETING_IMAGE.getLogMessage(), identity))
                .map(
                        identity -> Optional.of(identity.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(imageId);
                                            return format(IDENTITY_IMAGE_DELETED.getMessage(), identity.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteIdentityImageAsync(final Long identityId) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity.stream()
                                .peek(foundIdentity -> log.info(IDENTITY_FOUND_BEFORE_DELETING_IMAGE_ASYNC.getLogMessage(), foundIdentity))
                                .findFirst()
                                .map(
                                        foundIdentity -> Optional.of(foundIdentity.getImageId())
                                                .map(
                                                        imageId -> {
                                                            this.imageService.deleteImageAsync(imageId);
                                                            return format(IDENTITY_IMAGE_DELETED_ASYNC.getMessage(), foundIdentity.getId());
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .orElseThrow(
                                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                                )
                );
    }
}
