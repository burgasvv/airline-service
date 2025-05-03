package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.burgas.hotelbackend.log.ImageLogs.IMAGE_FOUND_BY_ID;
import static org.burgas.hotelbackend.message.ImageMessages.IMAGE_NOT_FOUND;
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
}
