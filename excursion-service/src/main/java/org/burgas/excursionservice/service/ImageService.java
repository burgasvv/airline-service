package org.burgas.excursionservice.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionservice.entity.Image;
import org.burgas.excursionservice.exception.ImageNotFoundException;
import org.burgas.excursionservice.exception.MultipartFileIsEmptyException;
import org.burgas.excursionservice.repository.ImageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionservice.message.FileMessage.MULTIPART_IS_EMPTY;
import static org.burgas.excursionservice.message.ImageMessage.IMAGE_DELETED;
import static org.burgas.excursionservice.message.ImageMessage.IMAGE_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findById(final String imageId) {
        return this.imageRepository.findById(Long.valueOf(imageId))
                .orElseGet(Image::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Image> findByIdAsync(final String imageId) {
        return supplyAsync(() -> this.imageRepository.findById(Long.valueOf(imageId)))
                .thenApplyAsync(image -> image.orElseGet(Image::new));
    }

    public Image findImageDataById(final String imageId) {
        return this.imageRepository.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Image> findImageDataByIdAsync(final String imageId) {
        return supplyAsync(() -> this.imageRepository.findById(Long.valueOf(imageId)))
                .thenApplyAsync(image -> image.orElseGet(Image::new));
    }

    public Image uploadImage(final Part part) {
        return of(part)
                .map(
                        file -> {
                            try {
                                return this.imageRepository.save(
                                        Image.builder()
                                                .name(file.getSubmittedFileName())
                                                .contentType(file.getContentType())
                                                .size(file.getSize())
                                                .format(requireNonNull(file.getContentType()).split("/")[0])
                                                .data(file.getInputStream().readAllBytes())
                                                .build()
                                );

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(() -> new MultipartFileIsEmptyException(MULTIPART_IS_EMPTY.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Image> uploadImageAsync(final Part part) {
        return supplyAsync(() -> this.uploadImage(part));
    }

    public Image changeImage(final String imageId, final Part part) {
        return this.imageRepository.findById(Long.valueOf(imageId))
                .map(
                        image -> {
                            try {
                                return this.imageRepository.save(
                                        Image.builder()
                                                .id(image.getId())
                                                .name(part.getSubmittedFileName())
                                                .contentType(part.getContentType())
                                                .size(part.getSize())
                                                .format(requireNonNull(part.getContentType()).split("/")[0])
                                                .data(part.getInputStream().readAllBytes())
                                                .build()
                                );

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(() -> new MultipartFileIsEmptyException(MULTIPART_IS_EMPTY.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Image> changeImageAsync(final String imageId, final Part part) {
        return supplyAsync(() -> this.changeImage(imageId, part));
    }

    public String deleteImage(final String imageId) {
        return this.imageRepository.findById(Long.valueOf(imageId))
                .map(
                        image -> {
                            this.imageRepository.deleteById(image.getId());
                            return IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<String> deleteImageAsync(final String imageId) {
        return supplyAsync(() -> this.deleteImage(imageId));
    }
}
