package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.exception.MultipartFileIsEmptyException;
import org.burgas.hotelbackend.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.ImageLogs.IMAGE_FOUND_BY_ID;
import static org.burgas.hotelbackend.log.ImageLogs.IMAGE_FOUND_BY_ID_ASYNC;
import static org.burgas.hotelbackend.message.ImageMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findById(final Long imageId) {
        return this.imageRepository.findById(imageId)
                .stream()
                .peek(image -> log.info(IMAGE_FOUND_BY_ID.getLogMessage(), image))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Image> findByIdAsync(final Long imageId) {
        return supplyAsync(() -> this.imageRepository.findById(imageId))
                .thenApplyAsync(
                        image -> image.stream()
                                .peek(foundImage -> log.info(IMAGE_FOUND_BY_ID_ASYNC.getLogMessage(), foundImage))
                                .findFirst()
                                .orElseGet(Image::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Image uploadImage(final MultipartFile multipartFile) {
        return of(multipartFile)
                .filter(file -> !file.isEmpty())
                .map(
                        file -> {
                            try {
                                return this.imageRepository.save(
                                        Image.builder()
                                                .name(file.getOriginalFilename())
                                                .contentType(file.getContentType())
                                                .size(file.getSize())
                                                .format(requireNonNull(file.getContentType()).split("/")[0])
                                                .data(file.getBytes())
                                                .build()
                                );

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(
                        () -> new MultipartFileIsEmptyException(MULTIPART_FILE_EMPTY.getMessage())
                );
    }

    @Async(value = "TaskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Image> uploadImageAsync(final MultipartFile multipartFile) {
        return supplyAsync(() -> this.uploadImage(multipartFile));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Image changeImage(final Long imageId, final MultipartFile multipartFile) {
        return of(multipartFile)
                .filter(file -> !file.isEmpty())
                .map(
                        file -> this.imageRepository.findById(imageId)
                                .map(
                                        image -> {
                                            try {
                                                return this.imageRepository.save(
                                                        Image.builder()
                                                                .id(image.getId())
                                                                .name(file.getOriginalFilename())
                                                                .contentType(file.getContentType())
                                                                .size(file.getSize())
                                                                .format(requireNonNull(file.getContentType()).split("/")[0])
                                                                .data(file.getBytes())
                                                                .build()
                                                );

                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .orElseThrow(
                        () -> new MultipartFileIsEmptyException(MULTIPART_FILE_EMPTY.getMessage())
                );
    }

    @Async(value = "TaskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Image> changeImageAsync(final Long imageId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.changeImage(imageId, multipartFile));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final Long imageId) {
        return this.imageRepository.findById(imageId)
                .map(
                        image -> {
                            this.imageRepository.deleteById(image.getId());
                            return IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "TaskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final Long imageId) {
        return supplyAsync(() -> this.deleteImage(imageId));
    }
}
