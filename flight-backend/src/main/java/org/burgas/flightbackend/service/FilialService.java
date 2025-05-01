package org.burgas.flightbackend.service;

import org.burgas.flightbackend.dto.FilialRequest;
import org.burgas.flightbackend.dto.FilialResponse;
import org.burgas.flightbackend.exception.AddressNotCreatedException;
import org.burgas.flightbackend.exception.FilialNotCreatedException;
import org.burgas.flightbackend.exception.FilialNotFoundException;
import org.burgas.flightbackend.mapper.AddressMapper;
import org.burgas.flightbackend.mapper.FilialMapper;
import org.burgas.flightbackend.repository.AddressRepository;
import org.burgas.flightbackend.repository.FilialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.flightbackend.log.FilialLogs.*;
import static org.burgas.flightbackend.message.AddressMessages.ADDRESS_NOT_CREATED;
import static org.burgas.flightbackend.message.FilialMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
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
    private final AddressMapper addressMapper;

    public FilialService(
            FilialRepository filialRepository, FilialMapper filialMapper,
            AddressRepository addressRepository, AddressMapper addressMapper
    ) {
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Scheduled(timeUnit = SECONDS, fixedRate = 50)
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void checkFilialSchedule() {
        this.filialRepository.findAll()
                .forEach(
                        filial -> {
                            LocalTime now = LocalTime.now();
                            filial.setOpened(now.isAfter(filial.getOpensAt()) && now.isBefore(filial.getClosesAt()));
                            this.filialRepository.save(filial);
                        }
                );
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
        return this.filialRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name"))
                .map(this.filialMapper::toFilialResponse);
    }

    public List<FilialResponse> findByCountryId(final String countryId) {
        return this.filialRepository.findFilialsByCountryId(Long.valueOf(countryId))
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BY_COUNTRY_ID.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<FilialResponse>> findByCountryIdAsync(final String countryId) {
        return supplyAsync(() -> this.filialRepository.findFilialsByCountryId(Long.parseLong(countryId)))
                .thenApplyAsync(
                        filials -> filials.stream()
                                .peek(filial -> log.info(FILIAL_FOUND_BY_COUNTRY_ID_ASYNC.getLogMessage(), filial))
                                .map(this.filialMapper::toFilialResponse)
                                .collect(Collectors.toList())
                );
    }

    public List<FilialResponse> findByCityId(final String cityId) {
        return this.filialRepository.findFilialsByCityId(Long.valueOf(cityId))
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BY_CITY_ID.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<FilialResponse>> findByCityIdAsync(final String cityId) {
        return supplyAsync(() -> this.filialRepository.findFilialsByCityId(Long.parseLong(cityId)))
                .thenApplyAsync(
                        filials -> filials.stream()
                                .peek(filial -> log.info(FILIAL_FOUND_BY_CITY_ID_ASYNC.getLogMessage(), filial))
                                .map(this.filialMapper::toFilialResponse)
                                .collect(Collectors.toList())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialResponse createOrUpdate(final FilialRequest filialRequest) {
        return of(this.addressMapper.toAddress(filialRequest.getAddress()))
                .map(this.addressRepository::save)
                .map(
                        address -> {
                            filialRequest.getAddress().setId(address.getId());
                            return of(this.filialMapper.toFilial(filialRequest))
                                    .map(this.filialRepository::save)
                                    .map(this.filialMapper::toFilialResponse)
                                    .orElseThrow(
                                            () -> new FilialNotCreatedException(FILIAL_NOT_CREATED.getMessage())
                                    );
                        }
                )
                .orElseThrow(
                        () -> new AddressNotCreatedException(ADDRESS_NOT_CREATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<FilialResponse> createOrUpdateAsync(final FilialRequest filialRequest) {
        return supplyAsync(() -> this.addressMapper.toAddress(filialRequest.getAddress()))
                .thenApplyAsync(this.addressRepository::save)
                .thenApplyAsync(
                        address -> {
                            filialRequest.getAddress().setId(address.getId());
                            try {
                                return supplyAsync(() -> this.filialMapper.toFilial(filialRequest))
                                        .thenApplyAsync(this.filialRepository::save)
                                        .thenApplyAsync(this.filialMapper::toFilialResponse)
                                        .get();

                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String filialId) {
        return this.filialRepository.findById(Long.parseLong(filialId))
                .map(
                        filial -> {
                            this.filialRepository.deleteById(filial.getId());
                            return FILIAL_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String filialId) {
        return supplyAsync(() -> this.filialRepository.findById(Long.parseLong(filialId)))
                .thenApplyAsync(
                        filial -> filial.stream()
                                .peek(foundFilial -> log.info(FILIAL_FOUND_BEFORE_DELETE.getLogMessage(), foundFilial))
                                .map(
                                        foundFilial -> {
                                            this.filialRepository.deleteById(foundFilial.getId());
                                            return FILIAL_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage())
                                )
                );
    }
}
