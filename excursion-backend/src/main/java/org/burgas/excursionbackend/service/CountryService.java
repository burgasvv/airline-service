package org.burgas.excursionbackend.service;

import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.dto.CountryRequest;
import org.burgas.excursionbackend.dto.CountryResponse;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.exception.CountryImageNotFoundException;
import org.burgas.excursionbackend.exception.CountryNotFoundException;
import org.burgas.excursionbackend.mapper.CountryMapper;
import org.burgas.excursionbackend.repository.CountryRepository;
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
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.CountryLogs.*;
import static org.burgas.excursionbackend.message.CountryMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CountryService {

    private static final Logger log = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final ImageService imageService;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper, ImageService imageService) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.imageService = imageService;
    }

    public List<CountryResponse> findAll() {
        return this.countryRepository.findAll()
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_ALL.getLogMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<CountryResponse>> findAllAsync() {
        return supplyAsync(this.countryRepository::findAll)
                .thenApplyAsync(
                        countries -> countries.stream()
                                .peek(country -> log.info(COUNTRY_FOUND_ALL_ASYNC.getLogMessage(), country))
                                .map(this.countryMapper::toCountryResponse)
                                .toList()
                );
    }

    public Page<CountryResponse> findAllPages(final Integer page, final Integer size) {
        return this.countryRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.ASC, "name"))
                .map(this.countryMapper::toCountryResponse);
    }

    public CountryResponse findById(final String countryId) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .stream()
                .peek(country -> log.info(COUNTRY_FOUND_BY_ID.getLogMessage(), country))
                .map(this.countryMapper::toCountryResponse)
                .findFirst()
                .orElseGet(CountryResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<CountryResponse> findByIdAsync(final String countryId) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.stream()
                                .peek(foundCountry -> log.info(COUNTRY_FOUND_BY_ID_ASYNC.getLogMessage(), foundCountry))
                                .map(this.countryMapper::toCountryResponse)
                                .findFirst()
                                .orElseGet(CountryResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CountryResponse createOrUpdate(final CountryRequest countryRequest) {
        return ofNullable(this.countryMapper.toCountry(countryRequest))
                .map(this.countryRepository::save)
                .map(this.countryMapper::toCountryResponse)
                .orElseGet(CountryResponse::new);
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<CountryResponse> createOrUpdateAsync(final CountryRequest countryRequest) {
        return supplyAsync(() -> this.countryMapper.toCountry(countryRequest))
                .thenApplyAsync(this.countryRepository::save)
                .thenApplyAsync(this.countryMapper::toCountryResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String countryId) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .map(
                        country -> {
                            log.info(COUNTRY_FOUND_BEFORE_DELETING.getLogMessage(), country);
                            this.countryRepository.deleteById(country.getId());
                            return COUNTRY_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String countryId) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.map(
                                foundCountry -> {
                                    log.info(COUNTRY_FOUND_BEFORE_DELETING.getLogMessage(), country);
                                    this.countryRepository.deleteById(foundCountry.getId());
                                    return COUNTRY_DELETED.getMessage();
                                }
                        )
                                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(final String countryId, final Part part) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .map(
                        country -> {
                            Image image = this.imageService.uploadImage(part);
                            country.setImageId(image.getId());
                            this.countryRepository.save(country);
                            return format(IMAGE_UPLOADED.getMessage(), image.getId());
                        }
                )
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadImageAsync(final String countryId, final Part part) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.map(
                                foundCountry -> {
                                    try {
                                        Image image = this.imageService.uploadImageAsync(part).get();
                                        foundCountry.setImageId(image.getId());
                                        this.countryRepository.save(foundCountry);
                                        return format(IMAGE_UPLOADED_ASYNC.getMessage(), image.getId());

                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(final String countryId, final Part part) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .map(
                        country -> of(country.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(String.valueOf(imageId), part);
                                            return format(IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(() -> new CountryImageNotFoundException(COUNTRY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeImageAsync(final String countryId, final Part part) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.map(
                                        foundCountry -> of(foundCountry.getImageId())
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
                                                .orElseThrow(() -> new CountryImageNotFoundException(COUNTRY_IMAGE_NOT_FOUND.getMessage()))
                                )
                                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final String countryId) {
        return this.countryRepository.findById(Long.valueOf(countryId))
                .map(
                        country -> of(country.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(String.valueOf(imageId));
                                            return format(IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(() -> new CountryImageNotFoundException(COUNTRY_IMAGE_NOT_FOUND.getMessage()))
                )
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteImageAsync(final String countryId) {
        return supplyAsync(() -> this.countryRepository.findById(Long.valueOf(countryId)))
                .thenApplyAsync(
                        country -> country.map(
                                        foundCountry -> of(foundCountry.getImageId())
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
                                                .orElseThrow(() -> new CountryImageNotFoundException(COUNTRY_IMAGE_NOT_FOUND.getMessage()))
                        )
                                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_NOT_FOUND.getMessage()))
                );
    }
}
