package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.FilialRequest;
import org.burgas.hotelbackend.dto.FilialResponse;
import org.burgas.hotelbackend.entity.Filial;
import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.FilialNotFoundException;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.mapper.FilialMapper;
import org.burgas.hotelbackend.repository.AddressRepository;
import org.burgas.hotelbackend.repository.FilialRepository;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.FilialLogs.*;
import static org.burgas.hotelbackend.message.FilialMessages.*;
import static org.burgas.hotelbackend.message.ImageMessages.IMAGE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FilialService {

    private static final Logger log = LoggerFactory.getLogger(FilialService.class);
    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;
    private final AddressRepository addressRepository;
    private final ImageService imageService;

    public FilialService(FilialRepository filialRepository, FilialMapper filialMapper, AddressRepository addressRepository, ImageService imageService) {
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.addressRepository = addressRepository;
        this.imageService = imageService;
    }

    public List<FilialResponse> findAll() {
        return this.filialRepository.findAll()
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_ALL.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<FilialResponse>> findAllAsync() {
        return supplyAsync(this.filialRepository::findAll)
                .thenApplyAsync(
                        filials -> filials.stream()
                                .peek(filial -> log.info(FILIAL_FOUND_ALL_ASYNC.getLogMessage(), filial))
                                .map(this.filialMapper::toFilialResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<FilialResponse> findAllPages(final Integer page, final Integer size) {
        return this.filialRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "id"))
                .map(this.filialMapper::toFilialResponse);
    }

    public FilialResponse findById(final Long filialId) {
        return this.filialRepository.findById(filialId)
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BY_ID.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .findFirst()
                .orElseGet(FilialResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<FilialResponse> findByIdAsync(final Long filialId) {
        return supplyAsync(() -> this.filialRepository.findById(filialId))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BY_ID_ASYNC.getLogMessage(), foundFilial))
                                .map(this.filialMapper::toFilialResponse)
                                .findFirst()
                                .orElseGet(FilialResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialResponse createOrUpdate(final FilialRequest filialRequest) {
        return of(this.filialMapper.toFilialSave(filialRequest))
                .stream()
                .peek(filial -> log.info(FILIAL_CREATED_OR_UPDATED.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .findFirst()
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<FilialResponse> createOrUpdateAsync(final FilialRequest filialRequest) {
        return supplyAsync(() -> this.filialMapper.toFilialSave(filialRequest))
                .thenApplyAsync(
                        filial -> of(filial).stream()
                                .peek(createdOrUpdatedFilial -> log.info(FILIAL_CREATED_OR_UPDATED_ASYNC.getLogMessage(), createdOrUpdatedFilial))
                                .map(this.filialMapper::toFilialResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long filialId) {
        return this.filialRepository.findById(filialId)
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BEFORE_DELETE.getLogMessage(), filial))
                .map(
                        filial -> {
                            if (filial.getAddressId() != null)
                                this.addressRepository.deleteById(filial.getAddressId());

                            this.filialRepository.deleteById(filial.getId());
                            return FILIAL_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long filialId) {
        return supplyAsync(() -> this.filialRepository.findById(filialId))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundFilial))
                                .map(
                                        foudnFilial -> {
                                            if (foudnFilial.getAddressId() != null)
                                                this.addressRepository.deleteById(foudnFilial.getAddressId());

                                            this.filialRepository.deleteById(foudnFilial.getId());
                                            return FILIAL_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadFilialImage(final Long filialId, final MultipartFile multipartFile) {
        return this.filialRepository.findById(filialId)
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BEFORE_UPLOAD_IMAGE.getLogMessage(), filial))
                .map(
                        filial -> {
                            Image image = this.imageService.uploadImage(multipartFile);
                            filial.setImageId(image.getId());
                            Filial saved = this.filialRepository.save(filial);
                            return format(FILIAL_IMAGE_UPLOADED.getMessage(), saved.getImageId());
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadFilialImageAsync(final Long filialId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.filialRepository.findById(filialId))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), foundFilial))
                                .map(
                                        foundFilial -> {
                                            try {
                                                Image image = this.imageService.uploadImageAsync(multipartFile).get();
                                                foundFilial.setImageId(image.getId());
                                                Filial saved = this.filialRepository.save(foundFilial);
                                                return format(FILIAL_IMAGE_UPLOADED.getMessage(), saved.getImageId());

                                            } catch (InterruptedException | ExecutionException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeFilialImage(final Long filialId, final MultipartFile multipartFile) {
        return this.filialRepository.findById(filialId)
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BEFORE_CHANGE_IMAGE.getLogMessage(), filial))
                .map(
                        filial -> of(filial.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(imageId, multipartFile);
                                            return format(FILIAL_IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeFilialImageAsync(final Long filialId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.filialRepository.findById(filialId))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), foundFilial))
                                .map(
                                        foundFilial -> of(foundFilial.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                Image image = this.imageService.changeImageAsync(imageId, multipartFile).get();
                                                                return format(FILIAL_IMAGE_CHANGED.getMessage(), image.getId());

                                                            } catch (InterruptedException | ExecutionException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteFilialImage(final Long filialId) {
        return this.filialRepository.findById(filialId)
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BEFORE_DELETE_IMAGE.getLogMessage(), filial))
                .map(
                        filial -> of(filial.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(imageId);
                                            return format(FILIAL_IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteFilialImageAsync(final Long filialId) {
        return supplyAsync(() -> this.filialRepository.findById(filialId))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), foundFilial))
                                .map(
                                        foundFilial -> of(foundFilial.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                this.imageService.deleteImageAsync(imageId).get();
                                                                return String.format(FILIAL_IMAGE_DELETED.getMessage(), imageId);

                                                            } catch (InterruptedException | ExecutionException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }
}
