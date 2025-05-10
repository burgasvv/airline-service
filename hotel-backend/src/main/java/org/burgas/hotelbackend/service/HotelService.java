package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.HotelRequest;
import org.burgas.hotelbackend.dto.HotelResponse;
import org.burgas.hotelbackend.entity.Hotel;
import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.HotelNotFoundException;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.mapper.HotelMapper;
import org.burgas.hotelbackend.repository.HotelRepository;
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
import static org.burgas.hotelbackend.log.HotelLogs.*;
import static org.burgas.hotelbackend.message.HotelMessages.*;
import static org.burgas.hotelbackend.message.ImageMessages.IMAGE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final ImageService imageService;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper, ImageService imageService) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
        this.imageService = imageService;
    }

    public List<HotelResponse> findAll() {
        return this.hotelRepository.findAll()
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_ALL.getLogMessage(), hotel))
                .map(this.hotelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<HotelResponse>> findAllAsync() {
        return supplyAsync(this.hotelRepository::findAll)
                .thenApplyAsync(
                        hotels -> hotels.stream()
                                .peek(hotel -> log.info(HOTEL_FOUND_ALL_ASYNC.getLogMessage(), hotel))
                                .map(this.hotelMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<HotelResponse> findAllPages(final Integer page, final Integer size) {
        return this.hotelRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "name"))
                .map(this.hotelMapper::toResponse);
    }

    public HotelResponse findById(final Long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BY_ID.getLogMessage(), hotel))
                .map(this.hotelMapper::toResponse)
                .findFirst()
                .orElseGet(HotelResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<HotelResponse> findByIdAsync(final Long hotelId) {
        return supplyAsync(() -> this.hotelRepository.findById(hotelId))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BY_ID_ASYNC.getLogMessage(), foundHotel))
                                .map(this.hotelMapper::toResponse)
                                .findFirst()
                                .orElseGet(HotelResponse::new)
                );
    }

    public HotelResponse findByName(final String name) {
        return this.hotelRepository.findHotelByName(name)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BY_NAME.getLogMessage(), hotel))
                .map(this.hotelMapper::toResponse)
                .findFirst()
                .orElseGet(HotelResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<HotelResponse> findByNameAsync(final String name) {
        return supplyAsync(() -> this.hotelRepository.findHotelByName(name))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BY_NAME_ASYNC.getLogMessage(), foundHotel))
                                .map(this.hotelMapper::toResponse)
                                .findFirst()
                                .orElseGet(HotelResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public HotelResponse createOrUpdate(final HotelRequest hotelRequest) {
        return of(this.hotelMapper.toEntity(hotelRequest))
                .map(this.hotelRepository::save)
                .stream()
                .peek(hotel -> log.info(HOTEL_CREATED_OR_UPDATED.getLogMessage(), hotel))
                .map(this.hotelMapper::toResponse)
                .findFirst()
                .orElseGet(HotelResponse::new);
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<HotelResponse> createOrUpdateAsync(final HotelRequest hotelRequest) {
        return supplyAsync(() -> this.hotelMapper.toEntity(hotelRequest))
                .thenApplyAsync(this.hotelRepository::save)
                .thenApplyAsync(
                        hotel -> of(hotel)
                                .stream()
                                .peek(foundHotel -> log.info(HOTEL_CREATED_OR_UPDATED_ASYNC.getLogMessage(), foundHotel))
                                .map(this.hotelMapper::toResponse)
                                .findFirst()
                                .orElseGet(HotelResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BEFORE_DELETE.getLogMessage(), hotel))
                .map(
                        hotel -> {
                            this.hotelRepository.deleteById(hotel.getId());
                            return HOTEL_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long hotelId) {
        return supplyAsync(() -> this.hotelRepository.findById(hotelId))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundHotel))
                                .map(
                                        foundHotel -> {
                                            this.hotelRepository.deleteById(foundHotel.getId());
                                            return HOTEL_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new HotelNotFoundException(HOTEL_DELETED.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadHotelImage(final Long hotelId, final MultipartFile multipartFile) {
        return this.hotelRepository.findById(hotelId)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BEFORE_UPLOAD_IMAGE.getLogMessage(), hotel))
                .map(
                        hotel -> {
                            Image image = this.imageService.uploadImage(multipartFile);
                            hotel.setImageId(image.getId());
                            Hotel saved = this.hotelRepository.save(hotel);
                            return format(HOTEL_IMAGE_UPLOADED.getMessage(), saved.getImageId());
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadHotelImageAsync(final Long hotelId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.hotelRepository.findById(hotelId))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC.getLogMessage(), foundHotel))
                                .map(
                                        foundHotel -> {
                                            try {
                                                Image image = this.imageService.uploadImageAsync(multipartFile).get();
                                                foundHotel.setImageId(image.getId());
                                                Hotel saved = this.hotelRepository.save(foundHotel);
                                                return format(HOTEL_IMAGE_UPLOADED.getMessage(), saved.getImageId());

                                            } catch (InterruptedException | ExecutionException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeHotelImage(final Long hotelId, final MultipartFile multipartFile) {
        return this.hotelRepository.findById(hotelId)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BEFORE_CHANGE_IMAGE.getLogMessage(), hotel))
                .map(
                        hotel -> Optional.of(hotel.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(imageId, multipartFile);
                                            return format(HOTEL_IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeHotelImageAsync(final Long hotelId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.hotelRepository.findById(hotelId))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), foundHotel))
                                .map(
                                        foundHotel -> Optional.of(foundHotel.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                Image image = this.imageService.changeImageAsync(imageId, multipartFile).get();
                                                                return String.format(HOTEL_IMAGE_CHANGED.getMessage(), image.getId());

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
                                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteHotelImage(final Long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .stream()
                .peek(hotel -> log.info(HOTEL_FOUND_BEFORE_DELETE_IMAGE.getLogMessage(), hotel))
                .map(
                        hotel -> Optional.of(hotel.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(imageId);
                                            return String.format(HOTEL_IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteHotelImageAsync(final Long hotelId) {
        return supplyAsync(() -> this.hotelRepository.findById(hotelId))
                .thenApplyAsync(
                        hotel -> hotel.stream()
                                .peek(foundHotel -> log.info(HOTEL_FOUND_BEFORE_DELETE_IMAGE_ASYNC.getLogMessage(), foundHotel))
                                .map(
                                        foundHotel -> Optional.of(foundHotel.getImageId())
                                                .map(
                                                        imageId -> {
                                                            this.imageService.deleteImageAsync(imageId);
                                                            return String.format(HOTEL_IMAGE_DELETED.getMessage(), imageId);
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new HotelNotFoundException(HOTEL_NOT_FOUND.getMessage())
                                )
                );
    }
}
