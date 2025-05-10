package org.burgas.excursionbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.dto.CityRequest;
import org.burgas.excursionbackend.dto.CityResponse;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.exception.CityImageNotFoundException;
import org.burgas.excursionbackend.exception.CityNotCreatedException;
import org.burgas.excursionbackend.exception.CityNotFoundException;
import org.burgas.excursionbackend.mapper.CityMapper;
import org.burgas.excursionbackend.repository.CityRepository;
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
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.CityLogs.*;
import static org.burgas.excursionbackend.message.CityMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CityService {

    private static final Logger log = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final ImageService imageService;

    public CityService(CityRepository cityRepository, CityMapper cityMapper, ImageService imageService) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.imageService = imageService;
    }

    public List<CityResponse> findAll() {
        return this.cityRepository.findAll()
                .stream()
                .peek(city -> log.info(CITY_FOUND_OF_ALL.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<CityResponse>> findAllAsync() {
        return supplyAsync(this.cityRepository::findAll)
                .thenApplyAsync(
                        cities -> cities.stream()
                                .peek(city -> log.info(CITY_FOUND_OF_ALL_ASYNC.getLogMessage(), city))
                                .map(this.cityMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<CityResponse> findAllPages(final Integer page, final Integer size) {
        return this.cityRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name"))
                .map(this.cityMapper::toResponse);
    }

    public CityResponse findById(final String cityId) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .stream()
                .peek(city -> log.info(CITY_FOUND_BY_ID.getLogMessage(), city))
                .map(this.cityMapper::toResponse)
                .findFirst()
                .orElseGet(CityResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CityResponse> findByIdAsync(final String cityId) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.stream()
                                .peek(foundCity -> log.info(CITY_FOUND_BY_ID_ASYNC.getLogMessage(), foundCity))
                                .map(this.cityMapper::toResponse)
                                .findFirst()
                                .orElseGet(CityResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CityResponse createOrUpdate(final CityRequest cityRequest) {
        return ofNullable(this.cityMapper.toEntity(cityRequest))
                .map(this.cityRepository::save)
                .map(this.cityMapper::toResponse)
                .orElseThrow(
                        () -> new CityNotCreatedException(CITY_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CityResponse> createOrUpdateAsync(final CityRequest cityRequest) {
        return supplyAsync(() -> this.cityMapper.toEntity(cityRequest))
                .thenApplyAsync(this.cityRepository::save)
                .thenApplyAsync(this.cityMapper::toResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String cityId) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .map(
                        city -> {
                            log.info(CITY_FOUND_BEFORE_DELETING.getLogMessage(), city);
                            this.cityRepository.deleteById(city.getId());
                            return CITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String cityId) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.map(
                                foundCity -> {
                                    log.info(CITY_FOUND_BEFORE_DELETING_ASYNC.getLogMessage(), foundCity);
                                    this.cityRepository.deleteById(foundCity.getId());
                                    return CITY_DELETED.getMessage();
                                }
                        )
                                .orElseThrow(() -> new CityNotFoundException(CITY_DELETED.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String cityId, final Part part) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .map(
                        city -> {
                            Image image = this.imageService.uploadImage(part);
                            city.setImageId(image.getId());
                            this.cityRepository.save(city);
                            return format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String cityId, final Part part) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.map(
                                foundCity -> {
                                    try {
                                        Image image = this.imageService.uploadImageAsync(part).get();
                                        foundCity.setImageId(image.getId());
                                        this.cityRepository.save(foundCity);
                                        return format(IMAGE_UPLOADED_ASYNC.getMessage(), image.getId());

                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String cityId, final Part part) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .map(
                        city -> of(city.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(String.valueOf(imageId), part);
                                            return format(IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(() -> new CityImageNotFoundException(CITY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String cityId, final Part part) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.map(
                                foundCity -> of(foundCity.getImageId())
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
                                        .orElseThrow(() -> new CityImageNotFoundException(CITY_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String cityId) {
        return this.cityRepository.findById(Long.valueOf(cityId))
                .map(
                        city -> of(city.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new CityImageNotFoundException(CITY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String cityId) {
        return supplyAsync(() -> this.cityRepository.findById(Long.valueOf(cityId)))
                .thenApplyAsync(
                        city -> city.map(
                                foundCity -> of(foundCity.getImageId())
                                        .map(
                                                imageId -> {
                                                    this.imageService.deleteImage(String.valueOf(imageId));
                                                    return format(IMAGE_DELETED_ASYNC.getMessage(), imageId);
                                                }
                                        )
                                        .orElseThrow(() -> new CityImageNotFoundException(CITY_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND.getMessage()))
                );
    }
}
