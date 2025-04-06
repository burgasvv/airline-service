package org.burgas.ticketservice.service;

import org.burgas.ticketservice.entity.Image;
import org.burgas.ticketservice.exception.ImageNotFoundException;
import org.burgas.ticketservice.repository.ImageRepository;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static org.burgas.ticketservice.message.ImageMessage.IMAGE_DELETED;
import static org.burgas.ticketservice.message.ImageMessage.IMAGE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Mono<Image> finById(final String imageId) {
        return this.imageRepository.findById(Long.valueOf(imageId));
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<Image> uploadImage(final FilePart filePart) {
        return filePart.content()
                .flatMap(
                        dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return this.imageRepository.save(
                                    Image.builder()
                                            .name(filePart.filename())
                                            .data(bytes)
                                            .isNew(true)
                                            .build()
                            );
                        }
                )
                .singleOrEmpty();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<Image> changeImage(final Long previousImageId, final FilePart filePart) {
        return this.imageRepository
                .findById(previousImageId)
                .flatMap(
                        image -> filePart.content().singleOrEmpty()
                                .flatMap(
                                        dataBuffer -> {
                                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                            dataBuffer.read(bytes);
                                            return this.imageRepository.save(
                                                    Image.builder()
                                                            .id(image.getId())
                                                            .name(filePart.filename())
                                                            .data(bytes)
                                                            .isNew(false)
                                                            .build()
                                            );
                                        }
                                )
                )
                .switchIfEmpty(
                        Mono.error(new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteImage(final Long imageId) {
        return this.imageRepository.findById(imageId)
                .flatMap(
                        image -> this.imageRepository.deleteById(requireNonNull(image.getId()))
                                .then(Mono.fromCallable(IMAGE_DELETED::getMessage))
                )
                .switchIfEmpty(
                        Mono.error(new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage()))
                );
    }
}
