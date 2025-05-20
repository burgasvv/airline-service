package org.burgas.flightbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.flightbackend.entity.Image;
import org.burgas.flightbackend.exception.ImageNotFoundException;
import org.burgas.flightbackend.exception.ImagePartNotFoundException;
import org.burgas.flightbackend.mapper.ImageMapper;
import org.burgas.flightbackend.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static java.util.Optional.of;
import static org.burgas.flightbackend.log.ImageLogs.IMAGE_FOUND_BY_ID;
import static org.burgas.flightbackend.message.ImageMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public Image findById(final String imageId) {
        return this.imageRepository.findById(Long.parseLong(imageId == null ? "0" : imageId))
                .stream()
                .peek(image -> log.info(IMAGE_FOUND_BY_ID.getLogMessage(), image))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Image uploadImage(final Part part) {
        return of(part)
                .map(
                        optPart -> {
                            try {
                                return this.imageRepository.save(
                                        this.imageMapper.uploadImagePart(optPart)
                                );

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(() -> new ImagePartNotFoundException(FILE_IS_EMPTY.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Image changeImage(final Long imageId, final Part part) {
        return this.imageRepository.findById(imageId == null ? 0L : imageId)
                .map(
                        image -> of(part)
                                .map(
                                        optPart -> {
                                            try {
                                                return this.imageRepository.save(
                                                        this.imageMapper.changeImagePart(image.getId(), optPart)
                                                );

                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .orElseThrow(() -> new ImagePartNotFoundException(FILE_IS_EMPTY.getMessage()))
                )
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }

    @SuppressWarnings("UnusedReturnValue")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final Long imageId) {
        return this.imageRepository.findById(imageId == null ? 0L : imageId)
                .map(
                        image -> {
                            this.imageRepository.deleteById(image.getId());
                            return IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()));
    }
}
